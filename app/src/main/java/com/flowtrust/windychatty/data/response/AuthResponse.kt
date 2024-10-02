package com.flowtrust.windychatty.data.response
data class AuthResponse(
    val access_token: String,
    val refresh_token: String,
    val user_id: String,
    val is_user_exists: Boolean
)

data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val user_id: String,
)