package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import org.koin.core.KoinComponent
import org.koin.core.inject

class AuthSignUpViewModel: BaseViewModel(), KoinComponent {

    /** Databinding properties */
    @get:Bindable
    var isProgress: Boolean by DelegatedBindable(false, BR.progress)

    @get:Bindable
    var email: String by DelegatedBindable("", BR.email, BR.buttonSignInEnable)

    @get:Bindable
    var password: String by DelegatedBindable("", BR.password, BR.buttonSignInEnable)

    @get:Bindable
    var passwordConfirm: String by DelegatedBindable("", BR.passwordConfirm, BR.buttonSignInEnable)

    @get:Bindable
    val buttonSignInEnable: Boolean
        get() = email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()

    @get:Bindable
    var error: String by DelegatedBindable("", BR.error, BR.errorVisible)

    @get:Bindable
    val errorVisible: Boolean
        get() = error.isNotEmpty()

    /** Private properties */
    private var resErrorNotMatch: String = ""

    /** Models */
    private val authRepository: AuthRepository by inject()

    /** Requests */
    private val requestSetPassword = MutableLiveData<String>()

    /** DataBinding methods */
    fun onSignUpButtonClicked() {
        if (password.isNotEmpty() && password != passwordConfirm) {
            error = resErrorNotMatch
            return
        }

        requestSetPassword.value = password
    }

    /** Responses */
    fun getSignUpResponseLive() = requestSetPassword.switchMap { password ->
        requestConvert<FirebaseUser, String> (
            method = { authRepository.createUser(email, password) },
            onLoading = { isProgress = true },
            onSuccess = { firebaseUser ->
                isProgress = false
                firebaseUser.uid
            },
            onError = { exception ->
                isProgress = false
                error = exception.localizedMessage ?: "Error"
            }
        )
    }

    /** Public method */
    fun initRes(errorNotMatch: String) {
        resErrorNotMatch = errorNotMatch
    }
}