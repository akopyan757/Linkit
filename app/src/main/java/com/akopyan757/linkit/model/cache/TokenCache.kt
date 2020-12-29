package com.akopyan757.linkit.model.cache

import android.content.SharedPreferences
import org.koin.core.KoinComponent
import org.koin.core.inject

class TokenCache: KoinComponent {

    companion object {
        private const val KEY_TOKEN = "KEY_TOKEN"
    }

    private val sharedPreferences: SharedPreferences by inject()

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }
}