package com.akopyan757.linkit_model_auth.cache

interface ITokenCache {
    fun saveToken(token: String)
    fun getToken(): String?
}