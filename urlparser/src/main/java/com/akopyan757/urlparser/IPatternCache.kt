package com.akopyan757.urlparser

interface IPatternCache<T : IPatternData> {
    fun getBasePattern(baseUrl: String): T?
    fun getSpecificPatterns(baseUrl: String): List<T>
    fun insertOrUpdate(data: T)
    fun removeByFolder(baseUrl: String)
    fun removeAll()
}