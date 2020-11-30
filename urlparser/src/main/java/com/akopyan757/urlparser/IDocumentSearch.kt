package com.akopyan757.urlparser

interface IDocumentSearch {
    suspend fun request(url: String): Boolean
    fun getUrl(): String
    fun getTitle(): String
    fun getLinkIconHref(rel: String, type: String? = null, sizes: String? = null): String
    fun getMetaDescription(): String
    fun getMetaKeywords(): String
    fun getMetaAuthor(): String
    fun getMetaByName(name: String): String
    fun getMetaByProperty(property: String): String
}