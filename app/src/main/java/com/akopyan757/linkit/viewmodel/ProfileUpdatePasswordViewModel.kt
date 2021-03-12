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
    var isProgress: Boolean by DelegatedBindable(false, BR.progress)

    @get:Bindable
    var oldPassword: String by DelegatedBindable("", BR.oldPassword, BR.buttonSignInEnable)

    @get:Bindable
    var password: String by DelegatedBindable("", BR.password, BR.buttonSignInEnable)

    @get:Bindable
    var passwordConfirm: String by DelegatedBindable("", BR.passwordConfirm, BR.buttonSignInEnable)

    @get:Bindable
    val buttonSignInEnable: Boolean
        get() = oldPassword.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()

    @get:Bindable
    var error: String by DelegatedBindable("", BR.error, BR.errorVisible)

    @get:Bindable
    val errorVisible: Boolean
        get() = error.isNotEmpty()

    fun getSetPasswordLiveResponseOrNull(): LiveData<ResponseState<Unit>>? {
        if (password.isNotEmpty() && password != passwordConfirm) {
            return null
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