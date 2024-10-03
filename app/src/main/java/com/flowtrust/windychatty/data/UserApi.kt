package com.flowtrust.windychatty.data

import com.flowtrust.windychatty.domain.models.UserData
import com.flowtrust.windychatty.domain.models.UserDataToUpdate
import com.flowtrust.windychatty.domain.remote.model.JWTResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT

interface UserApi {
    @GET("/api/v1/users/me")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<UserData>

    @PUT("/api/v1/users/me")
    suspend fun updateUserData(
        @Header("Authorization") token: String,
        @Body userData: UserDataToUpdate
    ):Response<Void>

    @GET("/api/v1/users/check-jwt")
    suspend fun checkToken(
        @Header("Authorization") token: String
    ): Response<JWTResponse>

}