package com.flowtrust.windychatty.ui.pages.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowtrust.windychatty.data.countryData.Country
import com.flowtrust.windychatty.data.request.RequestRefreshToken
import com.flowtrust.windychatty.domain.models.AuthRepository
import com.flowtrust.windychatty.domain.models.CountyRepository
import com.flowtrust.windychatty.domain.remote.model.ResultResponse
import com.flowtrust.windychatty.ui.pages.auth.contry_picker.getPhoneMaskUsingLibphonenumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository, // Репозиторий для работы с API
    private val countyRepository: CountyRepository // Репозиторий для работы с кодами стран
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            when(val listCountryResult = countyRepository.getCountries()){

                is ResultResponse.Success ->{
                    val listCountry = listCountryResult.data!!
                    when(val userCountryDataResult = countyRepository.getUserCountryInfo()){

                        is ResultResponse.Success -> {
                            val userCountryData = userCountryDataResult.data!!
                            val userCountry = listCountry.firstOrNull { it.isoCode == userCountryData.country }
                            _uiState.value = _uiState.value.copy(
                                listCountry = listCountry,
                                selectedCountry = userCountry,
                                code = userCountry?.dialCode?.drop(1)?:"", // так как там с +, его убираем
                                imgLink = userCountry?.flag?:"",
                                phoneMask = getPhoneMaskUsingLibphonenumber(userCountry?.isoCode?:"").drop((userCountry?.dialCode?.drop(1)?:"").length+2)
                            )
                            Log.d("listCountry",userCountryData.toString())
                        }

                        is ResultResponse.Error ->{
                            _uiState.value = _uiState.value.copy(
                                errorMessage = "Ошибка получения данных. Проверьте подключение к интернету!"
                            )
                        }
                    }
                }
                is ResultResponse.Error ->{
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "Ошибка получения данных. Проверьте подключение к интернету!"
                        )
                }
            }
        }
    }

   suspend fun skipAuth():Boolean{
      val token = authRepository.getRefreshToken()
       Log.d("fowkof",token.toString())
       try {
           val response = authRepository.refreshAccessToken(token?:"")
           Log.d("fowkof",response.toString())
           if (response is ResultResponse.Success) {
               val tokenData = response.data!!
                   Log.d("fowkof",tokenData.toString())
                   authRepository.saveTokens(tokenData.access_token,tokenData.refresh_token)
                   return true
               } else {
               Log.d("fowkof","all bad")
                return false
               }
       } catch (e: Exception) {
           Log.d("fowkof",e.toString())
           return false
       }



    }

    fun onChangeCountry(country: Country) {
        _uiState.value = _uiState.value.copy(
            selectedCountry = country,
            code = country.dialCode.drop(1),
            imgLink = country.flag,
            phoneMask = getPhoneMaskUsingLibphonenumber(country?.isoCode?:"").drop(country.dialCode.drop(1).length+2)

        )
    }

    // Обработка изменения номера телефона
    fun onPhoneChanged(phone: String) {
        _uiState.value = _uiState.value.copy(phone = phone.filter { it.isDigit() })
    }

    // Обработка изменения кода подтверждения
    fun onCodeChanged(code: String) {
        val country = _uiState.value.listCountry.firstOrNull{it.dialCode == "+$code"}
        _uiState.value = _uiState.value.copy(
            code = code,
            imgLink = country?.flag?:"",
            selectedCountry = country ,
            phoneMask = getPhoneMaskUsingLibphonenumber(country?.isoCode?:"").drop(code.length+2)
            )
        Log.d("onCodeChanged",getPhoneMaskUsingLibphonenumber(country?.isoCode?:""))

    }

    // Отправка номера телефона
    fun submitPhone(onSuccess:()->Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = "")
            try {
                val response = authRepository.sendPhoneNumber( "+${_uiState.value.code + _uiState.value.phone}")
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(isCodeSent = true)
                    onSuccess.invoke()
                } else {
                    _uiState.value = _uiState.value.copy(errorMessage = "Ошибка отправки кода")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Ошибка: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}