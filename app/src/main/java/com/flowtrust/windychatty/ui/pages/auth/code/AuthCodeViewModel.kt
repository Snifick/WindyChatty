package com.flowtrust.windychatty.ui.pages.auth.code

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowtrust.windychatty.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthCodeViewModel @Inject constructor(
    private val authRepository: AuthRepository, // Репозиторий для работы с API
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthCodeUiState())
    val uiState = _uiState.asStateFlow()
    fun submitCode(code: String,phoneCode:String,phoneNumber:String, goForward:()->Unit,goRegister:()->Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")
            try {
                val response = authRepository.verifyCode("+${phoneCode + phoneNumber}",code)
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null) {
                        if(authResponse.is_user_exists){
                            authRepository.saveTokens(authResponse.access_token, authResponse.refresh_token)
                            goForward()
                        }
                        else{
                            goRegister()
                        }
                    } else {
                        _uiState.value = _uiState.value.copy(errorMessage = "Ошибка!")
                    }
                } else {
                    _uiState.value = _uiState.value.copy(errorMessage = "Неверный код!")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Ошибка: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}