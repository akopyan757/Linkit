package com.akopyan757.linkit

import android.content.Intent
import android.util.Log
import com.akopyan757.linkit.model.firebase.AuthWrapper
import com.akopyan757.linkit.network.CustomTokenApi
import com.akopyan757.linkit.network.CustomTokenRequest
import com.akopyan757.linkit.view.service.IAuthorizationService
import com.google.firebase.auth.FirebaseUser
import com.huawei.hms.support.account.result.AuthAccount
import org.koin.core.KoinComponent
import org.koin.core.inject


class HmsAuthorizationService: IAuthorizationService, KoinComponent {

    companion object {
        private const val TAG = "HMS_AUTH_SERVICE"
    }

    private val authWrapper: AuthWrapper by inject()

    private val customTokenApi: CustomTokenApi by inject()

    override fun getSignInIntent(): Intent = authWrapper.getHuaweiSignInIntent()

    override suspend fun getUserAfterAuthorization(data: Intent?): FirebaseUser {
        val huaweiAccount = authWrapper.getHuaweiUserFromData(data)
        val customToken = createCustomToken(huaweiAccount)
        return if (customToken != null) {
            authWrapper.signInWithCustomToken(customToken)
        } else {
            throw Exception("Custom token could not create")
        }
    }

    override suspend fun signOut() {
        authWrapper.huaweiSignOut()
    }

    private suspend fun createCustomToken(account: AuthAccount): String? {
        val request = CustomTokenRequest(
            account.idToken, account.unionId, account.avatarUriString,
            account.displayName, account.email,
        )

        Log.i(TAG, "createCustomToken: start: $request")
        return try {
            customTokenApi.createCustomToken(request).firebaseToken
        } catch(e: Exception) {
            Log.e(TAG, "createCustomToken: failure", e)
            null
        }
    }
}