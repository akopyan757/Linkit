package com.akopyan757.linkit.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit_domain.usecase.auth.LinkPasswordUseCase
import org.koin.core.KoinComponent

class ProfileSetPasswordViewModel: BaseViewModel(), KoinComponent {

    private val linkPassword: LinkPasswordUseCase by injectUseCase()

    @get:Bindable var isProgress: Boolean by DB(false, BR.progress)
    @get:Bindable var password: String by DB("", BR.password, BR.buttonSignInEnable)
    @get:Bindable var passwordConfirm: String by DB("", BR.passwordConfirm, BR.buttonSignInEnable)

    @get:Bindable
    val buttonSignInEnable: Boolean get() = password.isNotEmpty() && passwordConfirm.isNotEmpty()

    @get:Bindable
    var error: String by DB("", BR.error, BR.errorVisible)

    @get:Bindable
    val errorVisible: Boolean
        get() = error.isNotEmpty()

    private val errorMessageResLive = MutableLiveData<@StringRes Int>()

    fun getErrorResLive(): LiveData<Int> {
        return errorMessageResLive
    }

    fun setPassword() {
        if (password.isNotEmpty() && password != passwordConfirm) {
            errorMessageResLive.value = R.string.error_passwords_match
            return
        }

        isProgress = true
        linkPassword.execute(LinkPasswordUseCase.Params(password),
            onSuccess = {
                isProgress = false
                emitAction(ACTION_LINK_SUCCESS)
            },
            onError = { exception ->
                isProgress = false
                error = exception.localizedMessage ?: Config.ERROR
            }
        )
    }

    fun setErrorMessage(message: String) {
        error = message
    }

    companion object {
        const val ACTION_LINK_SUCCESS = 132_1
    }
}