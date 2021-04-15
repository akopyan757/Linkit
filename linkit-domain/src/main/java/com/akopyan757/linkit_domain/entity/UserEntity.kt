package com.akopyan757.linkit_domain.entity

data class UserEntity(
    val uid: String,
    val email: String,
    val emailVerified: Boolean,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val hasEmailProvider: Boolean
)