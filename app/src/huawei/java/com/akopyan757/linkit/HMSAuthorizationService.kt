package com.akopyan757.linkit

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.akopyan757.linkit.network.CustomTokenApi
import com.akopyan757.linkit.network.CustomTokenRequest
import com.akopyan757.linkit.view.service.IAuthorizationService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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


class HMSAuthorizationService(
    private val activity: Activity
): IAuthorizationService, KoinComponent {

    private val authParams: AccountAuthParams by lazy {
        AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setEmail()
            .setProfile()
            .setIdToken()
            .createParams()
    }

    private val customTokenApi: CustomTokenApi by inject()

    private val huaweiService: AccountAuthService by lazy {
        AccountAuthManager.getService(activity.applicationContext, authParams)
    }

    override fun getSignInIntent(): Intent = huaweiService.signInIntent

    override suspend fun getUserAfterAuthorization(data: Intent?): FirebaseUser {
        val huaweiAccount = getHuaweiUserFromData(data)
        val customToken = createCustomToken(huaweiAccount)
        return signInWithCustomToken(customToken)
    }

    override suspend fun signOut() = suspendCoroutine<Unit> { cont ->
        huaweiService.signOut().addOnSuccessListener {
            Log.i(TAG, "signOut: OnSuccessListener")
            cont.resume(Unit)
        }?.addOnFailureListener { exception ->
            Log.e(TAG, "signOut: OnFailureListener", exception)
            cont.resumeWithException(exception)
        }
    }

    private suspend fun getHuaweiUserFromData(data: Intent?): AuthAccount = suspendCoroutine { cont ->
        val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
        if (authAccountTask.isSuccessful) {
            val account = authAccountTask.result
            Log.i(TAG, "getHuaweiUserFromData: account=$account")
            cont.resume(authAccountTask.result)
        } else {
            cont.resumeWithException(authAccountTask.exception)
        }
    }

    private suspend fun createCustomToken(account: AuthAccount): String {
        val request = CustomTokenRequest(
            account.idToken, account.getUnionId(), account.avatarUriString,
            account.displayName, account.email,
        )
        return customTokenApi.createCustomToken(request).firebaseToken
    }

    private suspend fun signInWithCustomToken(token: String) = suspendCoroutine<FirebaseUser> { cont ->
        FirebaseAuth.getInstance()
            .signInWithCustomToken(token)
            .addOnSuccessListener(activity) { authResult ->
                val user = authResult.user
                if (user != null) {
                    Log.i(TAG, "signInWithCustomToken: Firebase: success: user: $user")
                    cont.resume(user)
                } else {
                    cont.resumeWithException(Exception("User is not found."))
                }
            }.addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }

    companion object {
        private const val TAG = "HMSAuthorizationService"
    }
}