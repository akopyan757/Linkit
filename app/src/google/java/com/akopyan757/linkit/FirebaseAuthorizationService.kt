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
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FirebaseAuthorizationService(private val activity: Activity): IAuthorizationService {

    private val options: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private lateinit var client: GoogleSignInClient

    override fun signIn() {
        client = GoogleSignIn.getClient(activity.applicationContext, options)
        val intent = client.signInIntent.apply {
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        }
        activity.startActivityForResult(intent, REQUEST_CODE)
        Log.i(TAG, "signIn")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): ApiResponse<Unit> {
        return if (requestCode == REQUEST_CODE) {
             try {
                 val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                 val account = task.getResult(ApiException::class.java)
                 Log.d(TAG, "signIn: onActivityResult: FirebaseAuthWithGoogle:" + account?.id)
                 ApiResponse.Success(Unit)
            } catch (e: ApiException) {
                 Log.e(TAG, "signIn: onActivityResult: Google sign in failed", e)
                 ApiResponse.Error(e)
            }
        } else ApiResponse.Error(Exception("Incorrect Response"))
    }

    override suspend fun signOut() = suspendCoroutine<Unit> { cont ->
        client.signOut().addOnSuccessListener {
            Log.i(TAG, "signOut: OnSuccessListener")
            cont.resume(Unit)
        }.addOnFailureListener { exception ->
            Log.e(TAG, "signOut: OnFailureListener", exception)
            cont.resumeWithException(exception)
        }
    }

    override suspend fun silentSignIn(): ApiResponse<Unit> = suspendCoroutine { cont ->
        client = GoogleSignIn.getClient(activity.applicationContext, options)
        client.silentSignIn().addOnSuccessListener { authAccount ->
            // Obtain the user's ID information.
            Log.i(TAG, "silentSignIn: displayName: " + authAccount.displayName)
            cont.resume(ApiResponse.Success(Unit))
        }.addOnFailureListener { exception ->
            val apiException = exception as ApiException
            Log.i(TAG, "silentSignIn: sign failed status:" + apiException.statusCode)
            cont.resume(ApiResponse.Error(exception))
        }
    }

    companion object {
        private const val REQUEST_CODE = 6666
        private const val TAG = "GMSAuthorizationService"
    }
}