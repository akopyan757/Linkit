package com.akopyan757.urlparser

import com.google.gson.Gson
import java.io.InputStreamReader
import java.lang.reflect.Type

object JsonPatternsParser {

    fun <T> parse(resourceName: String, type: Type): List<T> {
        val classLoader = Thread.currentThread().contextClassLoader
        return classLoader.getResourceAsStream(resourceName)?.let { inputStream ->
            val reader = InputStreamReader(inputStream, "UTF-8")
            Gson().fromJson(reader, type)
        } ?: emptyList()
    }
}