package com.akopyan757.linkit_model_auth.network

import com.google.gson.annotations.SerializedName

data class CustomTokenResponse(
    @SerializedName("firebase_token")
    val firebaseToken: String
)