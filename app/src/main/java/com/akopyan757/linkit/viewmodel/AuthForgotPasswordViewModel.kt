package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit_domain.usecase.auth.ResetPasswordUseCase
import org.koin.core.KoinComponent

class AuthForgotPasswordViewModel: BaseViewModel(), KoinComponent {

    private val resetPassword: ResetPasswordUseCase by injectUseCase()

    @get:Bindable var isProgress: Boolean by DB(false, BR.progress)
    @get:Bindable var email: String by DB("", BR.email, BR.buttonSignInEnable)
    @get:Bindable var error: String by DB("", BR.error, BR.errorVisible)

    @get:Bindable val buttonSignInEnable: Boolean
        get() = email.isNotEmpty()

    @get:Bindable val errorVisible: Boolean
        get() = error.isNotEmpty()

    private val throwableLive = MutableLiveData<Throwable>()

    fun getThrowableLive(): LiveData<Throwable> {
        return throwableLive
    }

    fun resetPassword() {
        isProgress = true
        resetPassword.execute(ResetPasswordUseCase.Params(email),
            onSuccess = {
                isProgress = false
                emitAction(ACTION_RESET_SUCCESS)
            }, onError = { throwable ->
                isProgress = false
                throwableLive.value = throwable
            }
        )
    }

    companion object {
        const val ACTION_RESET_SUCCESS = 1238
    }
}