package com.akopyan757.linkit_domain.entity

data class UrlLinkAppEntity(
    val appId: String?,
    val appName: String?,
    val url: String?
) {
    constructor(): this(null, null, null)
}