package com.flowtrust.windychatty.ui.pages.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowtrust.windychatty.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isUsernameValid = MutableStateFlow(true)
    val isUsernameValid: StateFlow<Boolean> = _isUsernameValid

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Валидация username
    fun onUsernameChange(username: String) {
        _isUsernameValid.value = validateUsername(username)
    }

    // Проверка корректности username
    private fun validateUsername(username: String): Boolean {
        Log.d("validateUsername",username)
        val regex = "^[a-zA-Z][a-zA-Z0-9_-]{2,15}$".toRegex()
        Log.d("validateUsername","username.matches(regex) == ${username.matches(regex)}")
        return username.matches(regex)
    }

    // Проверка валидности формы
    fun isFormValid(name: String, username: String): Boolean {
        return name.isNotBlank() && validateUsername(username)
    }

    // Регистрация пользователя
    fun registerUser(
        phone: String,
        name: String,
        username: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authRepository.register(phone, name, username)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("registerUser",it.toString())
                        Log.d("registerUser",it.access_token.toString())
                        Log.d("registerUser",it.refresh_token.toString())
                        authRepository.saveTokens(it.access_token, it.refresh_token)
                        onSuccess()
                    }
                } else {
                    onError("Ошибка: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Ошибка: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
