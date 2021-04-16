package com.akopyan757.linkit_model_auth.network

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface CustomTokenApi {

    @POST("createCustomToken")
    fun createCustomToken(@Body request: CustomTokenRequest): Single<CustomTokenResponse>
}