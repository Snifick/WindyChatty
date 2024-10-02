package com.flowtrust.windychatty.data.response

data class TokenRefreshResponse(
    val access_token: String,
    val refresh_token: String,
    val user_id: Int
)