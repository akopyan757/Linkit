package com.akopyan757.linkit.viewmodel

import android.content.Intent
import androidx.databinding.Bindable
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.repository.AuthRepository
import com.akopyan757.linkit.view.service.IAuthorizationService
import org.koin.core.KoinComponent
import org.koin.core.inject

class AuthSignInViewModel: BaseViewModel(), KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val authService: IAuthorizationService by inject()

    @get:Bindable var isProgress: Boolean by DB(false, BR.progress)
    @get:Bindable var email: String by DB("", BR.email, BR.buttonSignInEnable)
    @get:Bindable var password: String by DB("", BR.password, BR.buttonSignInEnable)
    @get:Bindable var error: String by DB("", BR.error, BR.errorVisible)

    @get:Bindable val buttonSignInEnable: Boolean
        get() = email.isNotEmpty() && password.isNotEmpty()

    @get:Bindable val errorVisible: Boolean
        get() = error.isNotEmpty()

    fun requestSignInWithEmail() = requestConvert(
        request = authRepository.signInWithEmail(email, password),
        onLoading = { isProgress = true },
        onError = { isProgress = false },
        onSuccess = { firebaseUser ->
            isProgress = false
            firebaseUser.uid
        }
    )

    fun requestSignInWithService(data: Intent?) = requestConvert(
        request = authRepository.signInWithData(data),
        onLoading = { isProgress = true },
        onSuccess = { firebaseUser ->
            isProgress = false
            return@requestConvert firebaseUser.uid
        }, onError = {
            isProgress = false
        }
    )

    fun getSignInIntent() = authService.getSignInIntent()
}