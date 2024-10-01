package com.flowtrust.windychatty.data.response

data class UserCountryInfo(
    val city: String,
    val country: String,
    val hostname: String,
    val ip: String,
    val loc: String,
    val org: String,
    val postal: String,
    val readme: String,
    val region: String,
    val timezone: String
)