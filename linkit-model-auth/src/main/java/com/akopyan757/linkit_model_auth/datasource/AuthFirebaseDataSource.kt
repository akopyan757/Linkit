package com.akopyan757.linkit_model_auth.datasource

import com.akopyan757.linkit_domain.entity.UserEntity
import com.akopyan757.linkit_domain.repository.IAuthDataSource
import com.akopyan757.linkit_model_auth.cache.ITokenCache
import com.akopyan757.linkit_model_auth.mapper.MapperDirect
import com.akopyan757.linkit_model_auth.throwable.EmailNotFoundException
import com.akopyan757.linkit_model_auth.throwable.TokenNotFoundException
import com.akopyan757.linkit_model_auth.throwable.UserNotFoundException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Single

class AuthFirebaseDataSource(
    private val tokenCache: ITokenCache,
    private val mapper: MapperDirect<FirebaseUser, UserEntity>,
): IAuthDataSource {

    override fun emailVerification(): Completable = Completable.create { emitter ->
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.sendEmailVerification()
                .addOnFailureListener(emitter::onError)
                .addOnSuccessListener { emitter.onComplete() }
        } else {
            emitter.onError(UserNotFoundException())
        }
    }

    override fun resetPassword(email: String): Completable = Completable.create { emitter ->
        FirebaseAuth.getInstance()
            .sendPasswordResetEmail(email)
            .addOnFailureListener(emitter::onError)
            .addOnSuccessListener {
                emitter.onComplete()
            }
    }

    override fun signInWithEmail(email: String, password: String) = Single.create<UserEntity> { emitter ->
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnFailureListener(emitter::onError)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user != null) {
                    emitter.onSuccess(user.let(mapper::firstToSecond))
                } else {
                    emitter.onError(UserNotFoundException())
                }
            }
    }

    override fun createUser(email: String, password: String) = Single.create<UserEntity> { emitter ->
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result?.user
                if (user != null) {
                    emitter.onSuccess(user.let(mapper::firstToSecond))
                } else {
                    emitter.onError(UserNotFoundException())
                }
            }
            .addOnFailureListener(emitter::onError)
    }

    override fun reauthenticateCustomToken() = Completable.create { emitter ->
        val token = tokenCache.getToken()
        if (token != null) {
            FirebaseAuth.getInstance()
                .signInWithCustomToken(token)
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener(emitter::onError)
        } else {
            emitter.onError(TokenNotFoundException())
        }
    }

    override fun reauthenticateEmail(password: String) = Completable.create { emitter ->
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email
        if (user != null && email != null) {
            val credential = EmailAuthProvider.getCredential(email, password)
            user.reauthenticate(credential)
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener(emitter::onError)
        } else if (email == null) {
            emitter.onError(EmailNotFoundException())
        } else {
            emitter.onError(UserNotFoundException())
        }
    }

    override fun linkPasswordToAccount(password: String) = Single.create<UserEntity> { emitter ->
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email
        if (user != null && email != null) {
            val credential = EmailAuthProvider.getCredential(email, password)
            user.linkWithCredential(credential)
                .addOnSuccessListener { result ->
                    val userResult = result?.user
                    if (userResult != null) {
                        emitter.onSuccess(userResult.let(mapper::firstToSecond))
                    } else {
                        emitter.onError(UserNotFoundException())
                    }
                }
                .addOnFailureListener(emitter::onError)
        } else if (email == null) {
            emitter.onError(EmailNotFoundException())
        } else {
            emitter.onError(UserNotFoundException())
        }
    }

    override fun updatePassword(password: String) = Completable.create { emitter ->
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.updatePassword(password)
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener(emitter::onError)
        } else {
            emitter.onError(UserNotFoundException())
        }
    }

    override fun signOut() = Completable.fromAction {
        FirebaseAuth.getInstance().signOut()
    }

    override fun getUser(): Single<UserEntity> = Single.create { emitter ->
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            emitter.onSuccess(user.let(mapper::firstToSecond))
        } else {
            emitter.onError(UserNotFoundException())
        }
    }
}