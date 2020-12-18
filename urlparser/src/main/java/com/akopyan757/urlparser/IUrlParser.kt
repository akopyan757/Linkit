package com.akopyan757.urlparser

interface IUrlParser<V: UrlData> {
    suspend fun parseUrl(url: String): V
}