package com.flowtrust.windychatty.domain.remote.model

sealed class ResultResponse<out T> {
    data class Success<out T>(val data: T) : ResultResponse<T>()
    data class Error(val exception: Exception) : ResultResponse<Nothing>()
}