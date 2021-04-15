package com.akopyan757.linkit_domain.repository

import com.akopyan757.linkit_domain.entity.UserEntity

interface IAuthDataSource {
    fun createUser(email: String, password: String): UserEntity
    fun signInWithEmail(email: String, password: String): UserEntity
    fun linkPasswordToAccount(password: String): UserEntity
    fun resetPassword(email: String)
    fun updatePassword(oldPassword: String, newPassword: String)
    fun emailVerification()
    fun signOut()
    fun getUser(): UserEntity
}