package com.akopyan757.linkit.common.utils

import android.content.Context
import com.google.gson.Gson
import java.io.InputStreamReader
import java.lang.reflect.Type

class JsonPatternsParser(private val context: Context) {

    companion object {
        private const val CHARSET_NAME = "UTF-8"
    }

    fun <T> parse(resourceName: String, type: Type): List<T> {
        return context.assets.open(resourceName).use { stream ->
            val reader = InputStreamReader(stream, CHARSET_NAME)
            Gson().fromJson(reader, type)
        } ?: emptyList()
    }
}