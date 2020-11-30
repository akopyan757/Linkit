package com.akopyan757.base.model

import java.lang.Exception

sealed class ApiResponse<out T> {
    object Loading: ApiResponse<Nothing>()
    data class Success<out T>(val data: T): ApiResponse<T>()
    data class Error(val exception: Exception): ApiResponse<Nothing>()
}