package com.flowtrust.windychatty.data

import com.flowtrust.windychatty.data.request.RegisterRequest
import com.flowtrust.windychatty.data.request.RequestRefreshToken
import com.flowtrust.windychatty.data.request.SendAuthCodeRequest
import com.flowtrust.windychatty.data.request.SendAuthPhoneRequest
import com.flowtrust.windychatty.data.response.AuthResponse
import com.flowtrust.windychatty.data.response.TokenResponse
import com.flowtrust.windychatty.domain.remote.model.ResultResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(@Body request: SendAuthPhoneRequest): Response<Void>

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body request:SendAuthCodeRequest): Response<AuthResponse>

    @POST("/api/v1/users/refresh-token/")
    suspend fun refreshToken(@Body request: RequestRefreshToken): Response<TokenResponse>

    @POST("/api/v1/users/register/")
    suspend fun register(@Body request:RegisterRequest ): Response<TokenResponse>
}