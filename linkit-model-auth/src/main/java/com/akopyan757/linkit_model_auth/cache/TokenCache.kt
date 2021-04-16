package com.akopyan757.linkit_model_auth.cache

import android.content.SharedPreferences

class TokenCache(private val sharedPreferences: SharedPreferences): ITokenCache {

    companion object {
        private const val KEY_TOKEN = "KEY_TOKEN"
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply()
    }

    override fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }
}