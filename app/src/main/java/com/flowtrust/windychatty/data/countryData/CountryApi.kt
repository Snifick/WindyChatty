package com.flowtrust.windychatty.data.countryData

import retrofit2.Response
import retrofit2.http.GET

interface CountryApi {
    @GET("countries.json")
    suspend fun getCountries(): Response<List<Country>>
}
