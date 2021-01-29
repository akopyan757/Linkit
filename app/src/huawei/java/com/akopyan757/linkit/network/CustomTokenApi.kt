package com.akopyan757.linkit.network

import retrofit2.http.Body
import retrofit2.http.POST

interface CustomTokenApi {

    @POST("createCustomToken")
    suspend fun createCustomToken(@Body request: CustomTokenRequest): CustomTokenResponse
}