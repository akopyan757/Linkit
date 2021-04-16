package com.akopyan757.linkit.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit_domain.usecase.auth.UpdatePasswordUseCase
import org.koin.core.KoinComponent

class ProfileUpdatePasswordViewModel: BaseViewModel(), KoinComponent {

    private val updatePassword: UpdatePasswordUseCase by injectUseCase()

    /** Databinding properties */
    @get:Bindable var isProgress: Boolean by DB(false, BR.progress)
    @get:Bindable var oldPassword: String by DB("", BR.oldPassword, BR.buttonSignInEnable)
    @get:Bindable var password: String by DB("", BR.password, BR.buttonSignInEnable)
    @get:Bindable var passwordConfirm: String by DB("", BR.passwordConfirm, BR.buttonSignInEnable)

    @get:Bindable
    val buttonSignInEnable: Boolean
        get() = oldPassword.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()

    @get:Bindable
    var error: String by DB("", BR.error, BR.errorVisible)

    @get:Bindable
    val errorVisible: Boolean
        get() = error.isNotEmpty()

    private val errorMessageResLive = MutableLiveData<@StringRes Int>()
    private val throwableLive = MutableLiveData<Throwable>()

    fun getErrorResLive(): LiveData<Int> {
        return errorMessageResLive
    }

    fun getThrowableLive(): LiveData<Throwable> {
        return throwableLive
    }

    fun updatePassword() {
        if (password.isNotEmpty() && password != passwordConfirm) {
            errorMessageResLive.value = R.string.error_passwords_match
            return
        }

        isProgress = true
        updatePassword.execute(UpdatePasswordUseCase.Params(oldPassword, password),
            onSuccess = {
                isProgress = false
            }, onError = { throwable ->
                isProgress = false
                error = throwable.localizedMessage ?: "Error"
            }
        )
    }

    fun setErrorMessage(message: String) {
        error = message
    }

    companion object {
        const val ACTION_SUCCESS_UPDATE = 213_1
    }
}