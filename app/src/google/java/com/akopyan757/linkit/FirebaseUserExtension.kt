package com.akopyan757.linkit

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

object FirebaseUserExtension {
    
    fun existServiceProvider(firebaseUser: FirebaseUser): Boolean {
        return firebaseUser.providerData.find { userInfo ->
            userInfo.providerId == GoogleAuthProvider.PROVIDER_ID
        } != null
    }
}