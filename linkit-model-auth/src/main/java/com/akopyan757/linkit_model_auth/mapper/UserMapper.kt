package com.akopyan757.linkit_model_auth.mapper

import com.akopyan757.linkit_domain.entity.UserEntity
import com.akopyan757.linkit_model_auth.throwable.EmailNotFoundException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser


class UserMapper: Mapper<FirebaseUser, UserEntity> {

    override fun firstToSecond(data: FirebaseUser): UserEntity {
        return UserEntity(
            data.uid,
            data.email ?: throw EmailNotFoundException(),
            data.isEmailVerified,
            data.displayName,
            data.photoUrl?.toString(),
            data.hasEmailProvider()
        )
    }

    private fun FirebaseUser.hasEmailProvider(): Boolean {
        return providerData.find { userInfo ->
            userInfo.providerId == EmailAuthProvider.PROVIDER_ID
        } != null
    }
}