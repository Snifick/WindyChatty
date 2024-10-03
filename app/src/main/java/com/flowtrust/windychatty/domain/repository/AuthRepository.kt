package com.flowtrust.windychatty.domain.repository

import android.content.SharedPreferences
import com.flowtrust.windychatty.data.AuthApi
import com.flowtrust.windychatty.data.request.RegisterRequest
import com.flowtrust.windychatty.data.request.RequestRefreshToken
import com.flowtrust.windychatty.data.request.SendAuthCodeRequest
import com.flowtrust.windychatty.data.request.SendAuthPhoneRequest
import com.flowtrust.windychatty.data.response.AuthResponse
import com.flowtrust.windychatty.data.response.TokenResponse
import com.flowtrust.windychatty.domain.remote.model.ResultResponse
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi, // Retrofit API
    private val sharedPreferences: SharedPreferences // Хранилище для токенов
) {

    // Отправка номера телефона
    suspend fun sendPhoneNumber(phone: String): Response<Void> {
        return authApi.sendAuthCode(SendAuthPhoneRequest(phone))
    }

    // Проверка кода подтверждения
    suspend fun verifyCode(phone: String, code:String): Response<AuthResponse> {
        val requestBody = SendAuthCodeRequest(phone,code)
        return authApi.checkAuthCode(requestBody)
    }

    // Обновление токена
    suspend fun refreshAccessToken(token: String): ResultResponse<TokenResponse> {
        return try {
            val response = authApi.refreshToken(RequestRefreshToken(token))
            if (response.isSuccessful && response.body() != null) {
                ResultResponse.Success(response.body()!!)
            } else {
                ResultResponse.Error(Exception("Failed to refresh access token"))
            }
        } catch (e: Exception) {
            ResultResponse.Error(e)
        }
    }
    // Сохранение токенов
    fun saveTokens(accessToken: String, refreshToken: String) {
        with(sharedPreferences.edit()) {
            putString("accessToken", accessToken)
            putString("refreshToken", refreshToken)
            apply()
        }
    }

    // Получение access токена
    fun getAccessToken(): String? {
        return sharedPreferences.getString("accessToken", null)
    }

    // Получение refresh токена
    fun getRefreshToken(): String? {
        return sharedPreferences.getString("refreshToken", null)
    }

    // Удаление токенов при выходе
    fun clearTokens() {
        with(sharedPreferences.edit()) {
            remove("accessToken")
            remove("refreshToken")
            apply()
        }
    }

    // Регистрация пользователя
    suspend fun register(phone: String, name: String, username: String): Response<TokenResponse> {
        return authApi.register(RegisterRequest(name, phone, username))
    }


}