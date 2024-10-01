package com.flowtrust.windychatty.data

import com.flowtrust.windychatty.data.request.SendAuthCodeRequest
import com.flowtrust.windychatty.data.response.AuthResponse
import com.flowtrust.windychatty.data.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(@Body request: SendAuthCodeRequest): Response<Void>

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body phoneAndCode: Map<String, String>): Response<AuthResponse>

    @POST("/api/v1/users/refresh-token/")
    suspend fun refreshToken(@Header("Authorization") refreshToken: String): Response<TokenResponse>
}