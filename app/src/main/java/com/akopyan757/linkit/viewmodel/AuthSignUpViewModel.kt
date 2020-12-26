package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR

class AuthSignUpViewModel: BaseViewModel() {

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

    private var resErrorNotMatch: String = ""

    fun initRes(errorNotMatch: String) {
        resErrorNotMatch = errorNotMatch
    }

    fun startRegistration() {
        if (password.isNotEmpty() && password != passwordConfirm) {
            error = resErrorNotMatch
            return
        }

        this emitAction ACTION_REGISTRATION
    }

    companion object {
        const val ACTION_REGISTRATION = 121211
    }
}