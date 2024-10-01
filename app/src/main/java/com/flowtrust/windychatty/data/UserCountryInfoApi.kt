package com.flowtrust.windychatty.data

import com.flowtrust.windychatty.data.countryData.Country
import com.flowtrust.windychatty.data.response.UserCountryInfo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface UserCountryInfoApi {
    @GET("json")
    suspend fun getUserCountryInfo(): Response<UserCountryInfo>
}