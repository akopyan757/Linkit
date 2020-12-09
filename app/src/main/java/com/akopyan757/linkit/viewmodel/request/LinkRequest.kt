package com.akopyan757.linkit.viewmodel.request

data class LinkRequest(
    val title: String,
    val description: String,
    val link: String,
    val folderId: Int
)