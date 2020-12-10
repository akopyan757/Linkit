package com.akopyan757.linkit.common.utils

import android.content.Context
import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type

object JsonPatternsParser {

    fun <T> parse(context: Context, resourceName: String, type: Type): List<T> {
        return context.assets.open(resourceName).use { stream ->
            val reader = InputStreamReader(stream, "UTF-8")
            Gson().fromJson(reader, type)
        } ?: emptyList()
    }
}