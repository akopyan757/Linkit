package com.akopyan757.linkit.model.repository

import android.content.Intent
import com.akopyan757.base.model.BaseRepository
import com.akopyan757.linkit.common.Config
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

    fun createUser(email: String, password: String) = callIO {
        authWrapper.createUser(email, password)
    }

    fun signInWithEmail(email: String, password: String) = callIO {
        authWrapper.signInWithEmail(email, password)
    }

    fun linkPasswordToAccount(password: String) = callIO {
        authWrapper.reauthenticate()
        authWrapper.linkEmailToAccount(password)
    }

    fun resetPassword(email: String) = callIO {
        authWrapper.resetPassword(email)
    }

    fun signInWithData(data: Intent?) = callIO {
        authService.getUserAfterAuthorization(data)
    }

    fun emailVerification() = callIO {
        authWrapper.emailVerification()
    }

    fun getUser() = callIO {
        authWrapper.getUser() ?: throw Exception("User not found")
    }

    fun signOut() = callIO {
        authService.signOut()
        authWrapper.firebaseSignOut()
    }
}