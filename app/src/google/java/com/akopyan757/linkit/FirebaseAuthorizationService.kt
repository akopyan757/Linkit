package com.akopyan757.linkit

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.akopyan757.base.model.ApiResponse
import com.akopyan757.linkit.view.service.IAuthorizationService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth


class FirebaseAuthorizationService(private val activity: Activity): IAuthorizationService {

    private val options: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val client: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(activity, options)
    }

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun signIn() {
        activity.startActivityForResult(client.signInIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): ApiResponse<Unit> {
        return if (requestCode == REQUEST_CODE) {
             try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "firebaseAuthWithGoogle:" + account?.id)
                ApiResponse.Success(Unit)
            } catch (e: ApiException) {
                Log.e(TAG, "Google sign in failed", e)
                ApiResponse.Error(e)
            }
        } else ApiResponse.Error(Exception("Incorrect Response"))
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun silentSignIn(): ApiResponse<Unit> {
        val user = firebaseAuth.currentUser
        return if (user != null) {
            ApiResponse.Success(Unit)
        } else {
            ApiResponse.Error(Exception("User not found"))
        }
    }

    companion object {
        private const val REQUEST_CODE = 6666
        private const val TAG = "HMSAuthorizationService"
    }
}