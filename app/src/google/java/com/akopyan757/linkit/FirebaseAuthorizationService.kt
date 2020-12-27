package com.akopyan757.linkit

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.akopyan757.linkit.view.service.IAuthorizationService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FirebaseAuthorizationService(private val activity: Activity): IAuthorizationService {

    private val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(activity.applicationContext, googleSignInOptions)
    }

    override fun getSignInIntent(): Intent = googleSignInClient.signInIntent

    override suspend fun getUserAfterAuthorization(data: Intent?): FirebaseUser {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        val idToken = account?.idToken ?: throw Exception("Id token not found")
        return firebaseAuthWithGoogle(idToken)
    }

    override suspend fun signOut() = suspendCoroutine<Unit> { cont ->
        googleSignInClient.signOut().addOnSuccessListener {
            Log.i(TAG, "signOut: OnSuccessListener")
            cont.resume(Unit)
        }.addOnFailureListener { exception ->
            Log.e(TAG, "signOut: OnFailureListener", exception)
            cont.resumeWithException(exception)
        }
    }

    private suspend fun firebaseAuthWithGoogle(idToken: String): FirebaseUser = suspendCoroutine { cont ->
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance()
            .signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    if (user != null) {
                        Log.d(TAG, "signInWithCredential:firebase:success: $user")
                        cont.resume(user)
                    } else {
                        cont.resumeWithException(Exception("User not found"))
                    }
                } else {
                    val exception = task.exception
                    if (exception != null) {
                        Log.e(TAG, "signInWithCredential:firebase:failure", task.exception)
                        cont.resumeWithException(exception)
                    }
                }
            }
    }

    companion object {
        private const val TAG = "GMSAuthorizationService"
    }
}