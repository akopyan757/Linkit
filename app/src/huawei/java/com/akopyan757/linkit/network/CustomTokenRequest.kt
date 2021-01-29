package com.akopyan757.linkit.network

import com.google.gson.annotations.SerializedName

data class CustomTokenRequest(
    @SerializedName("id_token")
    val id_token: String,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("picture")
    val picture: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
)