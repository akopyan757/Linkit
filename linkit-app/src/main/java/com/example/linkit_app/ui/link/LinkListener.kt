package com.example.linkit_app.ui.link


interface LinkListener {
    fun onShareClick(link: LinkItem)
    fun onItemClick(link: LinkItem)
    fun onItemLongClick(link: LinkItem)
    fun onItemAppClick(app: AppData)
}