package com.flowtrust.windychatty.domain.models

import android.content.SharedPreferences
import com.flowtrust.windychatty.data.AuthApi
import com.flowtrust.windychatty.data.request.SendAuthCodeRequest
import com.flowtrust.windychatty.data.response.AuthResponse
import com.flowtrust.windychatty.data.response.TokenResponse
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApi: AuthApi, // Retrofit API
    private val sharedPreferences: SharedPreferences // Хранилище для токенов
) {

    // Отправка номера телефона
    suspend fun sendPhoneNumber(phone: String): Response<Void> {
        return authApi.sendAuthCode(SendAuthCodeRequest(phone))
    }

    // Проверка кода подтверждения
    suspend fun verifyCode(phone: String,): Response<AuthResponse> {
        val requestBody = mapOf("phone" to phone)
        return authApi.checkAuthCode(requestBody)
    }

    // Обновление токена
    suspend fun refreshAccessToken(): Response<TokenResponse>? {
        val refreshToken = getRefreshToken() ?: return null
        return authApi.refreshToken("Bearer $refreshToken")
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
}