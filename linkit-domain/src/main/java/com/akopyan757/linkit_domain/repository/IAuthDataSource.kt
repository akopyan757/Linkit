package com.akopyan757.linkit_domain.repository

import com.akopyan757.linkit_domain.entity.UserEntity
import io.reactivex.Completable
import io.reactivex.Single

interface IAuthDataSource {
    fun createUser(email: String, password: String): Single<UserEntity>
    fun signInWithEmail(email: String, password: String): Single<UserEntity>
    fun linkPasswordToAccount(password: String): Single<UserEntity>
    fun reauthenticateCustomToken(): Completable
    fun reauthenticateEmail(password: String): Completable
    fun resetPassword(email: String): Completable
    fun updatePassword(password: String): Completable
    fun emailVerification(): Completable
    fun signOut(): Completable
    fun getUser(): Single<UserEntity>
}