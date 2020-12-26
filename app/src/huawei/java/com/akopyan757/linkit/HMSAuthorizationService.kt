package com.akopyan757.linkit

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.akopyan757.base.model.ApiResponse
import com.akopyan757.linkit.view.service.IAuthorizationService
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class HMSAuthorizationService(private val activity: Activity): IAuthorizationService {

    private val authParams: AccountAuthParams by lazy {
        AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setAuthorizationCode()
            .createParams()
    }

    private var service: AccountAuthService? = null

    override fun signIn() {
        service = AccountAuthManager.getService(activity, authParams)
        activity.startActivityForResult(service?.signInIntent, REQUEST_CODE)
        Log.i(TAG, "signIn")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): ApiResponse<Unit> {
        return if (requestCode == REQUEST_CODE) {
            val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
            if (authAccountTask.isSuccessful) {
                // The sign-in is successful, and the user's ID information and authorization code are obtained.
                val authAccount = authAccountTask.result
                Log.i(TAG, "signIn: onActivityResult: Success" + authAccount.authorizationCode)
                ApiResponse.Success(Unit)
            } else {
                val exception = authAccountTask.exception as ApiException
                Log.e(TAG, "signIn: onActivityResult: Failed: " + exception.statusCode)
                ApiResponse.Error(exception)
            }
        } else ApiResponse.Error(Exception("Incorrect Response"))
    }

    override suspend fun signOut() = suspendCoroutine<Unit> { cont ->
        if (service == null) {
            cont.resume(Unit)
        }

        service?.signOut()?.addOnSuccessListener {
            Log.i(TAG, "signOut: OnSuccessListener")
            service = null
            cont.resume(Unit)
        }?.addOnFailureListener { exception ->
            Log.e(TAG, "signOut: OnFailureListener", exception)
            cont.resumeWithException(exception)
        }
    }

    override suspend fun silentSignIn() = suspendCoroutine<ApiResponse<Unit>> { cont ->
        service = AccountAuthManager.getService(activity, authParams)
        val task = service?.silentSignIn()
        task?.addOnSuccessListener(activity) { authAccount ->
            // Obtain the user's ID information.
            Log.i(TAG, "silentSignIn: displayName:" + authAccount.displayName)
            cont.resume(ApiResponse.Success(Unit))
        }
        task?.addOnFailureListener(activity) { exception ->
            val apiException = exception as ApiException
            Log.i(TAG, "silentSignIn: sign failed status:" + apiException.statusCode)
            cont.resumeWithException(exception)
        }
    }

    companion object {
        private const val REQUEST_CODE = 7777
        private const val TAG = "HMSAuthorizationService"
    }
}