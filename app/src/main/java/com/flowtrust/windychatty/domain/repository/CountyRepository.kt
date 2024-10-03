package com.flowtrust.windychatty.domain.repository

import android.util.Log
import com.flowtrust.windychatty.data.UserCountryInfoApi
import com.flowtrust.windychatty.data.countryData.Country
import com.flowtrust.windychatty.data.countryData.CountryApi
import com.flowtrust.windychatty.data.response.UserCountryInfo
import com.flowtrust.windychatty.domain.remote.model.ResultResponse
import javax.inject.Inject

class CountyRepository @Inject constructor(
    private val countryApi: CountryApi,
    private val userCountryInfoApi: UserCountryInfoApi
) {
    suspend fun getCountries(): ResultResponse<List<Country>> {
        return try {
            val response = countryApi.getCountries()
            if (response.isSuccessful && response.body() != null) {
                ResultResponse.Success(response.body()!!)
            } else {
                ResultResponse.Error(Exception("Failed to fetch countries"))
            }
        } catch (e: Exception) {
            ResultResponse.Error(e)
        }
    }

    suspend fun getUserCountryInfo(): ResultResponse<UserCountryInfo> {
        return try {
            val response = userCountryInfoApi.getUserCountryInfo()
            if (response.isSuccessful && response.body() != null) {
                ResultResponse.Success(response.body()!!)
            } else {
                ResultResponse.Error(Exception("Failed to fetch user country info"))
            }
        } catch (e: Exception) {
            ResultResponse.Error(e)
        }
    }
}