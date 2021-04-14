package com.akopyan757.linkit_domain.entity

data class FolderEntity(
    val id: String,
    val name: String,
    val order: Int
) {
    constructor() : this("", "", 0)
}