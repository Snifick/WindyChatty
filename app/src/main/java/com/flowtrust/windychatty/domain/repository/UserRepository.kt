package com.flowtrust.windychatty.domain.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.flowtrust.windychatty.data.AuthApi
import com.flowtrust.windychatty.data.UserApi
import com.flowtrust.windychatty.domain.models.UserData
import com.flowtrust.windychatty.domain.models.UserDataToUpdate
import com.flowtrust.windychatty.domain.remote.model.ResultResponse
import com.google.gson.Gson
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApi: UserApi,
    private val sharedPreferences: SharedPreferences,
    private val authRepository: AuthRepository
) {
    suspend fun getUserProfile(token: String): ResultResponse<UserData> {
        return try {
            // Получаем доступ к SharedPreferences

            // Проверяем, есть ли данные пользователя в SharedPreferences
            val cachedUserData = sharedPreferences.getString("user_data", null)

            if (cachedUserData != null) {
                // Данные есть в SharedPreferences, возвращаем их
                val userData = Gson().fromJson(cachedUserData, UserData::class.java)
                Log.d("fowf","DATABASE ${userData.profile_data.avatar}")
                ResultResponse.Success(userData)
            } else {
                // Данных нет, делаем запрос на сервер
                val isValid = userApi.checkToken("Bearer $token")
                if(isValid.isSuccessful){
                    val isValid = isValid.body()!!.is_valid
                    var validToken = token
                    if(!isValid) {
                        val refreshToken = authRepository.getRefreshToken()
                       val resp = authRepository.refreshAccessToken(refreshToken?:"")
                        if(resp is ResultResponse.Success){
                            validToken = resp.data.access_token
                            authRepository.saveTokens(validToken,resp.data.refresh_token)
                        }
                    }
                    val response = userApi.getUserProfile("Bearer $validToken")
                        if (response.isSuccessful && response.body() != null) {
                            // Сохраняем данные в SharedPreferences
                            val editor = sharedPreferences.edit()
                            val userDataJson = Gson().toJson(response.body())
                            editor.putString("user_data", userDataJson)
                            editor.apply()

                            ResultResponse.Success(response.body()!!)
                        } else {
                            ResultResponse.Error(Exception("Failed to load user profile"))
                        }

                }
                else ResultResponse.Error(Exception("Error"))

            }
        } catch (e: Exception) {
            ResultResponse.Error(e)
        }
    }
 fun saveUserDataToPreferences(userData: UserData) {
     Log.d("fowf","saveUserDataToPreferences ")
        val editor = sharedPreferences.edit()
        val userDataJson = Gson().toJson(userData)
     Log.d("fowf","DATABASE ${userData.profile_data.avatar}")
        editor.putString("user_data", userDataJson)
        editor.apply()
    }
    suspend fun updateUserData(token: String,userData: UserDataToUpdate):Boolean{
        try {
            val isValid = userApi.checkToken("Bearer $token")
            if(isValid.isSuccessful) {
                val isValid = isValid.body()!!.is_valid
                var validToken = token
                if (!isValid) {
                    val refreshToken = authRepository.getRefreshToken()
                    val resp = authRepository.refreshAccessToken(refreshToken ?: "")
                    if (resp is ResultResponse.Success) {
                        validToken = resp.data.access_token
                        authRepository.saveTokens(validToken, resp.data.refresh_token)
                    }
                }
                val response = userApi.updateUserData("Bearer $validToken", userData)
                if (response.isSuccessful) {
                    return true
                } else {
                    return false
                }
            }
            else{
                return false
            }
        }
        catch (e:Exception){
                return false
        }
    }
}