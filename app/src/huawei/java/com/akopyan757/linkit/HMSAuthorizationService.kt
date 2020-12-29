package com.akopyan757.linkit

import android.content.Intent
import com.akopyan757.linkit.model.firebase.AuthWrapper
import com.akopyan757.linkit.view.service.IAuthorizationService
import com.google.firebase.auth.FirebaseUser
import org.koin.core.KoinComponent
import org.koin.core.inject


class HMSAuthorizationService: IAuthorizationService, KoinComponent {

    private val authWrapper: AuthWrapper by inject()

    override fun getSignInIntent(): Intent = authWrapper.getHuaweiSignInIntent()

    override suspend fun getUserAfterAuthorization(data: Intent?): FirebaseUser {
        val huaweiAccount = authWrapper.getHuaweiUserFromData(data)
        val customToken = authWrapper.createCustomToken(huaweiAccount)
        return authWrapper.signInWithCustomToken(customToken)
    }

    override suspend fun signOut() {
        authWrapper.huaweiSignOut()
    }
}