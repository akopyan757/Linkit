package com.akopyan757.linkit.view.service

import android.content.Intent
import com.akopyan757.base.model.ApiResponse

interface IAuthorizationService {
    fun signIn()
    suspend fun signOut()
    suspend fun silentSignIn(): ApiResponse<Unit>
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): ApiResponse<Unit>
}