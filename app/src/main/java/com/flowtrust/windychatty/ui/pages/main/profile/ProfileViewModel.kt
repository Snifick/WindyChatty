package com.flowtrust.windychatty.ui.pages.main.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowtrust.windychatty.domain.models.Avatar
import com.flowtrust.windychatty.domain.models.UserData
import com.flowtrust.windychatty.domain.remote.model.ResultResponse
import com.flowtrust.windychatty.domain.repository.AuthRepository
import com.flowtrust.windychatty.domain.repository.UserRepository
import com.flowtrust.windychatty.ui.common.mappers.mapProfileDataToUserDataToUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

   private val _uiState:MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            val accessToken = authRepository.getAccessToken()?:""
            val result = userRepository.getUserProfile(accessToken)
            if(result is ResultResponse.Success){
                _uiState.value = ProfileUiState.Success(result.data!!)
            }
            else{
                _uiState.value = ProfileUiState.Error("Упс, что-то пошло не так...")
            }
        }
    }

    fun updateData(userData: UserData,avatar: Avatar){
        viewModelScope.launch {
            val accessToken = authRepository.getAccessToken()?:""
            _uiState.value =  ProfileUiState.Loading
            if(userRepository.updateUserData(accessToken,mapProfileDataToUserDataToUpdate(userData.profile_data,avatar))){
                userRepository.saveUserDataToPreferences(userData)
                _uiState.value = ProfileUiState.Success(userData)
            }
            else{
                _uiState.value =  ProfileUiState.Error("Ошибка при обновлении данных.")
            }
        }

    }


}