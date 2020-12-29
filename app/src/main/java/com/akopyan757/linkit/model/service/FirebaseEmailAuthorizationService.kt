package com.akopyan757.linkit.model.service

import android.util.Log
import com.akopyan757.base.model.ApiResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.core.KoinComponent
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FirebaseEmailAuthorizationService : KoinComponent {

    companion object {
        private const val TAG = "FIREBASE_EMAIL_AUTH"
    }

    fun getUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    suspend fun createUser(email: String, password: String): ApiResponse<FirebaseUser> = suspendCoroutine { cont ->
        try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
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
                .addOnCompleteListener { task ->
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

    suspend fun resetPassword(email: String) = suspendCoroutine<Unit> { cont ->
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Log.d(TAG, "Email sent ($email).")
                cont.resume(Unit)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Email error", exception)
                cont.resumeWithException(exception)
            }
    }

    fun sinOut() {
        FirebaseAuth.getInstance().signOut()
    }

}