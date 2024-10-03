package com.flowtrust.windychatty.domain.remote.model

data class JWTResponse(
    val errors: Boolean,
    val is_valid: Boolean
)