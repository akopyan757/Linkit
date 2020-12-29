package com.akopyan757.linkit.model.firebase

import android.content.Context
import android.content.Intent
import android.util.Log
import com.akopyan757.linkit.R
import com.akopyan757.linkit.model.cache.TokenCache
import com.akopyan757.linkit.model.service.network.CustomTokenApi
import com.akopyan757.linkit.network.CustomTokenRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.result.AuthAccount
import com.huawei.hms.support.account.service.AccountAuthService
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthWrapper(private val context: Context): KoinComponent {

    companion object {
        private const val TAG = "AUTH_WRAPPER"

        fun existsEmailProvider(firebaseUser: FirebaseUser): Boolean {
            return firebaseUser.providerData.find { userInfo ->
                userInfo.providerId == EmailAuthProvider.PROVIDER_ID
            } != null
        }
    }

    private val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val authParams: AccountAuthParams by lazy {
        AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setEmail()
            .setProfile()
            .setIdToken()
            .createParams()
    }


    private val googleService: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(context, googleSignInOptions)
    }

    private val huaweiService: AccountAuthService by lazy {
        AccountAuthManager.getService(context, authParams)
    }

    private val customTokenApi: CustomTokenApi by inject()

    private val tokenCache: TokenCache by inject()

    suspend fun emailVerification() = suspendCoroutine<String> { cont ->
        val user = FirebaseAuth.getInstance().currentUser
        user?.sendEmailVerification()
            ?.addOnSuccessListener {
                val email = user.email
                if (email != null) {
                    Log.i(TAG, "emailVerification: success: user: $user")
                    cont.resume(email)
                } else {
                    Log.i(TAG, "emailVerification: failure: user not found")
                    cont.resumeWithException(Exception("User has no email"))
                }
            }?.addOnFailureListener { exception ->
                Log.w(TAG, "emailVerification: failure", exception)
                cont.resumeWithException(exception)
            }
    }

    suspend fun signInWithCustomToken(token: String) = suspendCoroutine<FirebaseUser> { cont ->
        Log.i(TAG, "signInWithCustomToken: success: user: $token")
        FirebaseAuth.getInstance()
            .signInWithCustomToken(token)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    Log.i(TAG, "signInWithCustomToken: success: user: $user")
                    tokenCache.saveToken(token)
                    cont.resume(user)
                } else {
                    Log.i(TAG, "signInWithCustomToken: failure: user not found")
                    cont.resumeWithException(Exception("User is not found."))
                }
            }.addOnFailureListener { exception ->
                Log.w(TAG, "signInWithCustomToken: failure", exception)
                cont.resumeWithException(exception)
            }
    }

    suspend fun resetPassword(email: String) = suspendCoroutine<Unit> { cont ->
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Log.d(TAG, "resetPassword: email sent ($email).")
                cont.resume(Unit)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "resetPassword: failure: ", exception)
                cont.resumeWithException(exception)
            }
    }

    suspend fun signInWithEmail(email: String, password: String): FirebaseUser = suspendCoroutine { cont ->
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user != null) {
                    Log.i(TAG, "signInWithEmail: success: user: $user")
                    cont.resume(user)
                } else {
                    Log.i(TAG, "signInWithEmail: failure: user not found")
                    cont.resumeWithException(Exception("User is not found."))
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "signInWithEmail: failure", exception)
                cont.resumeWithException(exception)
            }
    }

    suspend fun createUser(email: String, password: String) = suspendCoroutine<FirebaseUser> { cont ->
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result?.user
                if (user != null) {
                    Log.i(TAG, "createUser: success: user: $user")
                    cont.resume(user)
                } else {
                    Log.i(TAG, "createUser: failure: User not found")
                    cont.resumeWithException(Exception("User is not found."))
                }
            }.addOnFailureListener { exception ->
                Log.w(TAG, "createUser: failure", exception)
                cont.resumeWithException(exception)
            }
    }

    suspend fun reauthenticateCustomToken() = suspendCoroutine<Unit> { cont ->
        val user = FirebaseAuth.getInstance().currentUser ?: throw Exception("User is not found.")
        val token = tokenCache.getToken() ?: throw Exception("Token is not found")
        FirebaseAuth.getInstance()
            .signInWithCustomToken(token)
            .addOnSuccessListener {
                Log.i(TAG, "reauthenticateCustomToken: success: user: $user")
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "reauthenticateCustomToken: failure", exception)
                cont.resumeWithException(exception)
            }
    }

    suspend fun reauthenticateEmail(password: String) = suspendCoroutine<Unit> { cont ->
        val user = FirebaseAuth.getInstance().currentUser ?: throw Exception("User is not found.")
        val email = user.email ?: throw Exception("Email is not found.")
        val credential = EmailAuthProvider.getCredential(email, password)
        user.reauthenticate(credential)
            .addOnSuccessListener {
                Log.i(TAG, "reauthenticateEmail: success: user: $user")
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "reauthenticateEmail: failure", exception)
                cont.resumeWithException(exception)
            }
    }

    suspend fun updatePassword(password: String) = suspendCoroutine<Unit> { cont ->
        val user = FirebaseAuth.getInstance().currentUser ?: throw Exception("User is not found.")
        user.updatePassword(password)
            .addOnSuccessListener {
                Log.i(TAG, "reauthenticateEmail: success: user: $user")
                cont.resume(Unit)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "reauthenticateEmail: failure", exception)
                cont.resumeWithException(exception)
            }
    }

    suspend fun linkEmailToAccount(password: String): FirebaseUser = suspendCoroutine { cont ->
        val user = FirebaseAuth.getInstance().currentUser ?: throw Exception("User is not found.")
        val email = user.email ?: throw Exception("Email is not found.")
        val credential = EmailAuthProvider.getCredential(email, password)
        user.linkWithCredential(credential)
            .addOnSuccessListener { result ->
                val userResult = result?.user
                if (userResult != null) {
                    Log.i(TAG, "linkEmailToAccount: success: user: $userResult")
                    cont.resume(user)
                } else {
                    Log.w(TAG, "linkEmailToAccount: failure: User not found")
                    cont.resumeWithException(Exception("User is not found."))
                }
            }.addOnFailureListener { exception ->
                Log.w(TAG, "linkEmailToAccount: failure", exception)
                cont.resumeWithException(exception)
            }
    }

    suspend fun createCustomToken(account: AuthAccount): String {
        val request = CustomTokenRequest(
            account.idToken, account.unionId, account.avatarUriString,
            account.displayName, account.email,
        )
        Log.i(TAG, "createCustomToken: start: $request")
        return try {
            customTokenApi.createCustomToken(request).also {
                Log.i(TAG, "createCustomToken=$it")
            }.firebaseToken
        } catch(e: Exception) {
            Log.e(TAG, "createCustomToken", e)
            ""
        }
    }

    fun getHuaweiSignInIntent(): Intent = huaweiService.signInIntent

    suspend fun getHuaweiUserFromData(data: Intent?): AuthAccount = suspendCoroutine { cont ->
        val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
        if (authAccountTask.isSuccessful) {
            val account = authAccountTask.result
            Log.i(TAG, "getHuaweiUserFromData: success: account=$account")
            cont.resume(account)
        } else {
            Log.w(TAG, "getHuaweiUserFromData: failure", authAccountTask.exception)
            cont.resumeWithException(authAccountTask.exception)
        }
    }

    suspend fun huaweiSignOut() = suspendCoroutine<Unit> { cont ->
        huaweiService.signOut().addOnSuccessListener {
            Log.i(TAG, "huaweiSignOut: OnSuccessListener")
            cont.resume(Unit)
        }?.addOnFailureListener { exception ->
            Log.w(TAG, "huaweiSignOut: OnFailureListener", exception)
            cont.resumeWithException(exception)
        }
    }

    fun getGoogleSignInIntent(): Intent = googleService.signInIntent
    
    suspend fun getGoogleUserFromData(data: Intent?): GoogleSignInAccount = suspendCoroutine { cont ->
        GoogleSignIn.getSignedInAccountFromIntent(data)
            .addOnSuccessListener { account ->
                Log.i(TAG, "getGoogleUserFromData: success: $account")
                cont.resume(account)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "getGoogleUserFromData: failure", exception)
                cont.resumeWithException(exception)
            }
    }

    suspend fun googleSignOut() = suspendCoroutine<Unit> { cont ->
        googleService.signOut().addOnSuccessListener {
            Log.i(TAG, "googleSignOut: success")
            cont.resume(Unit)
        }.addOnFailureListener { exception ->
            Log.w(TAG, "googleSignOut: error", exception)
            cont.resumeWithException(exception)
        }
    }

    suspend fun addGoogleCredentialToFirebase(idToken: String): FirebaseUser = suspendCoroutine { cont ->
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance()
            .signInWithCredential(credential)
            .addOnSuccessListener { result ->
                val user = result?.user
                if (user != null) {
                    Log.d(TAG, "addGoogleCredentialToFirebase: success: $user")
                    cont.resume(user)
                } else {
                    Log.d(TAG, "addGoogleCredentialToFirebase: failure: user not found")
                    cont.resumeWithException(Exception("User not found"))
                }
            }.addOnFailureListener { exception ->
                Log.e(TAG, "signInWithCredential:firebase:failure", exception)
                cont.resumeWithException(exception)
            }
    }

    fun getUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun firebaseSignOut() {
        FirebaseAuth.getInstance().signOut()
    }
}