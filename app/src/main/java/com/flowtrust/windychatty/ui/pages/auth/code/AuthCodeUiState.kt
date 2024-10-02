package com.flowtrust.windychatty.ui.pages.auth.code
data class AuthCodeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isVerified:Boolean = false
)