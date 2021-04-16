package com.akopyan757.linkit_model_auth

import android.content.Context
import android.content.Intent
import com.akopyan757.linkit_domain.entity.UserEntity
import com.akopyan757.linkit_domain_android_ext.datasource.IAuthIntentDataSource
import com.akopyan757.linkit_model_auth.cache.ITokenCache
import com.akopyan757.linkit_model_auth.mapper.MapperDirect
import com.akopyan757.linkit_model_auth.network.CustomTokenApi
import com.akopyan757.linkit_model_auth.network.CustomTokenRequest
import com.akopyan757.linkit_model_auth.throwable.CustomTokenCreateException
import com.akopyan757.linkit_model_auth.throwable.UserNotFoundException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.result.AuthAccount
import com.huawei.hms.support.account.service.AccountAuthService
import io.reactivex.Completable
import io.reactivex.Single

class AuthServiceDataSource(
    private val context: Context,
    private val customTokenApi: CustomTokenApi,
    private val tokenCache: ITokenCache,
    private val mapper: MapperDirect<FirebaseUser, UserEntity>
): IAuthIntentDataSource {

    private val huaweiService: AccountAuthService by lazy {
        AccountAuthManager.getService(context, authParams)
    }

    private val authParams: AccountAuthParams by lazy {
        AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setEmail()
            .setProfile()
            .setIdToken()
            .setAccessToken()
            .setAuthorizationCode()
            .createParams()
    }

    override fun getServiceSignInIntent() = Single.fromCallable {
        huaweiService.signInIntent
    }

    override fun getServiceUser(data: Intent?): Single<UserEntity> {
        return getHuaweiUserFromData(data)
            .flatMap(this::createCustomToken)
            .flatMap(this::signInWithCustomToken)
    }


    override fun signOut(): Completable = Completable.create { emitter ->
        huaweiService.signOut().addOnSuccessListener {
            emitter.onComplete()
        }?.addOnFailureListener { exception ->
            emitter.onError(exception)
        }
    }

    private fun getHuaweiUserFromData(data: Intent?) = Single.create<AuthAccount> { emitter ->
        val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
        if (authAccountTask.isSuccessful) {
            emitter.onSuccess(authAccountTask.result)
        } else {
            emitter.onError(CustomTokenCreateException())
        }
    }

    private fun createCustomToken(account: AuthAccount): Single<String> {
        val request = CustomTokenRequest(
            account.idToken, account.unionId, account.avatarUriString,
            account.displayName, account.email,
        )

        return customTokenApi.createCustomToken(request)
            .map { response -> response.firebaseToken }
    }


    private fun signInWithCustomToken(token: String) = Single.create<UserEntity> { emitter ->
        FirebaseAuth.getInstance()
            .signInWithCustomToken(token)
            .addOnFailureListener(emitter::onError)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    tokenCache.saveToken(token)
                    emitter.onSuccess(user.let(mapper::firstToSecond))
                } else {
                    emitter.onError(UserNotFoundException())
                }
            }
    }

}