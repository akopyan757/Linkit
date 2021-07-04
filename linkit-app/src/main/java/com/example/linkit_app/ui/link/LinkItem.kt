package com.example.linkit_app.ui.link

data class LinkItem(
    val id: String = "",
    val url: String = "",
    val title: String = "",
    val description: String = "",
    val site: String = "",
    val photoUrl: String? = "",
    val isPlayer: Boolean = false,
    val appData: AppData? = null
)