package com.akopyan757.linkit

import android.content.Intent
import com.akopyan757.linkit.model.firebase.AuthWrapper
import com.akopyan757.linkit.view.service.IAuthorizationService
import com.google.firebase.auth.FirebaseUser
import org.koin.core.KoinComponent
import org.koin.core.inject

class FirebaseAuthorizationService: IAuthorizationService, KoinComponent{

    private val authWrapper: AuthWrapper by inject()

    override fun getSignInIntent(): Intent = authWrapper.getGoogleSignInIntent()

    override suspend fun getUserAfterAuthorization(data: Intent?): FirebaseUser {
        val googleAccount = authWrapper.getGoogleUserFromData(data)
        val idToken = googleAccount.idToken ?: throw Exception("Id token not found")
        return authWrapper.addGoogleCredentialToFirebase(idToken)
    }

    override suspend fun signOut() {
        authWrapper.googleSignOut()
    }
}