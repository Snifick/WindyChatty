package com.flowtrust.windychatty.data.response
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val isUserExists: Boolean
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)