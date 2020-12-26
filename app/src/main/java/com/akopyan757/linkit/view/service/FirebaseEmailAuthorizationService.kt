package com.akopyan757.linkit.view.service

import android.app.Activity
import android.util.Log
import com.akopyan757.base.model.ApiResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.core.KoinComponent
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FirebaseEmailAuthorizationService(private val activity: Activity): KoinComponent {

    companion object {
        private const val TAG = "FIREBASE_EMAIL_AUTH"
    }

    fun getUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    suspend fun createUser(email: String, password: String): ApiResponse<FirebaseUser> = suspendCoroutine { cont ->
        try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUser: success: $email")
                        val user = task.result?.user ?: return@addOnCompleteListener
                        cont.resume(ApiResponse.Success(user))
                    } else {
                        val exception = task.exception ?: return@addOnCompleteListener
                        Log.e(TAG, "createUser: failure", exception)
                        cont.resume(ApiResponse.Error(exception))
                    }
                }
        } catch (exception: Exception) {
            cont.resume(ApiResponse.Error(exception))
        }
    }

    suspend fun signIn(email: String, password: String): ApiResponse<FirebaseUser> = suspendCoroutine { cont ->
        try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signIn: success: $email")
                        val user = task.result?.user ?: return@addOnCompleteListener
                        cont.resume(ApiResponse.Success(user))
                    } else {
                        val exception = task.exception ?: return@addOnCompleteListener
                        Log.e(TAG, "signIn: failure", exception)
                        cont.resume(ApiResponse.Error(exception))
                    }
                }
        } catch (exception: Exception) {
            cont.resume(ApiResponse.Error(exception))
        }
    }

    fun sinOut() {
        FirebaseAuth.getInstance().signOut()
    }

}