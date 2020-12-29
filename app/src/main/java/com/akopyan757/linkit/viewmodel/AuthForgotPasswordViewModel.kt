package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.repository.AuthRepository
import com.akopyan757.linkit.view.service.IAuthorizationService
import com.google.firebase.auth.FirebaseUser
import org.koin.core.KoinComponent
import org.koin.core.inject

class AuthForgotPasswordViewModel: BaseViewModel(), KoinComponent {

    /** Databinding properties */
    @get:Bindable
    var email: String by DelegatedBindable("", BR.email, BR.buttonSignInEnable)

    @get:Bindable
    val buttonSignInEnable: Boolean
        get() = email.isNotEmpty()

    @get:Bindable
    var error: String by DelegatedBindable("", BR.error, BR.errorVisible)

    @get:Bindable
    val errorVisible: Boolean
        get() = error.isNotEmpty()

    /** Models */
    private val authRepository: AuthRepository by inject()

    /** Requests */
    private val requestResetPassword = MutableLiveData<String>()

    /** DataBinding methods */
    fun onResetPasswordButtonClicked() {
        requestResetPassword.value = email
    }

    /** Responses */
    fun getResetPasswordResponseLive() = requestResetPassword.switchMap { email ->
        requestConvert<Unit, String> (
            method = { authRepository.resetPassword(email) },
            onSuccess = { email },
            onError = { exception -> error = exception.localizedMessage ?: "Error" }
        )
    }
}