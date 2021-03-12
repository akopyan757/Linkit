package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.repository.AuthRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ProfileUpdatePasswordViewModel: BaseViewModel(), KoinComponent {

    private val authRepository: AuthRepository by inject()

    /** Databinding properties */
    @get:Bindable
    var isProgress: Boolean by DB(false, BR.progress)

    @get:Bindable
    var oldPassword: String by DB("", BR.oldPassword, BR.buttonSignInEnable)

    @get:Bindable
    var password: String by DB("", BR.password, BR.buttonSignInEnable)

    @get:Bindable
    var passwordConfirm: String by DB("", BR.passwordConfirm, BR.buttonSignInEnable)

    @get:Bindable
    val buttonSignInEnable: Boolean
        get() = oldPassword.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()

    @get:Bindable
    var error: String by DB("", BR.error, BR.errorVisible)

    @get:Bindable
    val errorVisible: Boolean
        get() = error.isNotEmpty()

    fun requestSetPassword(): LiveData<ResponseState<Unit>> {
        if (password.isNotEmpty() && password != passwordConfirm) {
            return emptyLiveRequest()
        }

        return requestConvert (
            request = authRepository.updatePassword(oldPassword, password),
            onLoading = { isProgress = true },
            onSuccess = { isProgress = false },
            onError = { exception ->
                isProgress = false
                error = exception.localizedMessage ?: "Error"
            }
        )
    }

    fun setErrorMessage(message: String) {
        error = message
    }
}