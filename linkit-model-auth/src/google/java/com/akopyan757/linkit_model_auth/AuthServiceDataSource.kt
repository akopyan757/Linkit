package com.akopyan757.linkit_model_auth

import android.content.Context
import android.content.Intent
import com.akopyan757.linkit_domain.entity.UserEntity
import com.akopyan757.linkit_domain_android_ext.datasource.IAuthIntentDataSource
import com.akopyan757.linkit_model_auth.throwable.UserNotFoundException
import com.akopyan757.linkit_model_auth.mapper.MapperDirect
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.Completable
import io.reactivex.Single

class AuthServiceDataSource(
    private val context: Context,
    private val defaultWebClientId: String,
    private val mapper: MapperDirect<FirebaseUser, UserEntity>
): IAuthIntentDataSource {

    private val googleSignInOptions: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(defaultWebClientId)
            .requestEmail()
            .build()
    }

    private val googleService: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(context, googleSignInOptions)
    }

    override fun getServiceSignInIntent(): Single<Intent> = Single.fromCallable {
        googleService.signInIntent
    }

    override fun signOut(): Completable = Completable.create { emitter ->
        googleService.signOut().addOnSuccessListener {
            emitter.onComplete()
        }.addOnFailureListener { throwable ->
            emitter.onError(throwable)
        }
    }

    override fun getServiceUser(data: Intent?): Single<UserEntity> {
        return getGoogleUserFromData(data)
            .map { account -> account.idToken }
            .flatMap(this::addGoogleCredentialToFirebase)
            .map(mapper::firstToSecond)
    }


    private fun getGoogleUserFromData(data: Intent?) = Single.create<GoogleSignInAccount> { emitter ->
        GoogleSignIn.getSignedInAccountFromIntent(data)
            .addOnFailureListener(emitter::onError)
            .addOnSuccessListener(emitter::onSuccess)
    }

    private fun addGoogleCredentialToFirebase(idToken: String) = Single.create<FirebaseUser> { emitter ->
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance()
            .signInWithCredential(credential)
            .addOnFailureListener(emitter::onError)
            .addOnSuccessListener { result ->
                val user = result?.user
                if (user != null) {
                    emitter.onSuccess(user)
                } else {
                    emitter.onError(UserNotFoundException())
                }
            }
    }
}