package com.akopyan757.linkit.model.service.network

import com.akopyan757.linkit.network.CustomTokenRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomTokenApi {

    @POST("createCustomToken")
    suspend fun createCustomToken(@Body request: CustomTokenRequest): CustomTokenResponse
}