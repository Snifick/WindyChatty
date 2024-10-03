package com.flowtrust.windychatty.ui.pages.main.profile

import com.flowtrust.windychatty.domain.models.UserData

sealed interface ProfileUiState {

    data object Loading : ProfileUiState

    data class Error(val error:String) : ProfileUiState
    data class Success(val userData: UserData):ProfileUiState
}