package com.akopyan757.linkit.model.repository

import android.content.Intent
import com.akopyan757.base.model.BaseRepository
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.exception.FirebaseUserNotFound
import com.akopyan757.linkit.model.firebase.AuthWrapper
import com.akopyan757.linkit.view.service.IAuthorizationService
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class AuthRepository: BaseRepository(), KoinComponent {

    override val coroutineDispatcher: CoroutineDispatcher by inject(named(Config.IO_DISPATCHERS))

    private val authService: IAuthorizationService by inject()
    private val authWrapper: AuthWrapper by inject()

    fun createUser(email: String, password: String) = wrapActionIOWithResult {
        authWrapper.createUser(email, password)
    }

    fun signInWithEmail(email: String, password: String) = wrapActionIOWithResult {
        authWrapper.signInWithEmail(email, password)
    }

    fun linkPasswordToAccount(password: String) = wrapActionIOWithResult {
        authWrapper.reauthenticateCustomToken()
        authWrapper.linkEmailToAccount(password)
    }

    fun resetPassword(email: String) = wrapActionIO {
        authWrapper.resetPassword(email)
    }

    fun updatePassword(oldPassword: String, newPassword: String) = wrapActionIO {
        authWrapper.reauthenticateEmail(oldPassword)
        authWrapper.updatePassword(newPassword)
    }

    fun signInWithData(data: Intent?) = wrapActionIOWithResult {
        authService.getUserAfterAuthorization(data)
    }

    fun emailVerification() = wrapActionIOWithResult {
        authWrapper.emailVerification()
    }

    fun getUser() = wrapActionIOWithResult {
        authWrapper.getUser() ?: throw FirebaseUserNotFound()
    }

    fun signOut() = wrapActionIO {
        authService.signOut()
        authWrapper.firebaseSignOut()
    }
}