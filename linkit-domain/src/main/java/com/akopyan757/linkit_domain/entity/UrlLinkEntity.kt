package com.akopyan757.linkit_domain.entity

data class UrlLinkEntity(
    val id: String,
    val url: String,
    val title: String,
    val description: String,
    val photoUrl: String?,
    val logoUrl: String?,
    val folderId: String?,
    val order: Int
) {
    constructor() : this("", "", "", "", null, null, null, 0)
}