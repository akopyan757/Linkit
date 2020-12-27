package com.akopyan757.linkit.view.service

import android.content.Intent
import com.google.firebase.auth.FirebaseUser

interface IAuthorizationService {
    fun getSignInIntent(): Intent
    suspend fun signOut()
    suspend fun silentSignIn(): FirebaseUser
    suspend fun getUserAfterAuthorization(data: Intent?): FirebaseUser
}