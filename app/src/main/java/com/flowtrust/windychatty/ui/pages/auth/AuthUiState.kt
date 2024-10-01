package com.flowtrust.windychatty.ui.pages.auth

import com.flowtrust.windychatty.data.countryData.Country


data class AuthUiState(
    val phone: String = "",
    val phoneMask:String = "",
    val code: String = "",
    val imgLink:String = "",
    val isLoading: Boolean = false,
    val isCodeSent: Boolean = false,
    val errorMessage: String = "",
    val selectedCountry: Country? = null,
    val listCountry:List<Country> = listOf()
)