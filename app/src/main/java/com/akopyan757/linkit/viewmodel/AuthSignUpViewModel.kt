package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit_domain.usecase.auth.CreateUserUseCase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import org.koin.core.KoinComponent

class AuthSignUpViewModel: BaseViewModel(), KoinComponent {

    private val createUser: CreateUserUseCase by injectUseCase()

    @get:Bindable var isProgress: Boolean by DB(false, BR.progress)
    @get:Bindable var email: String by DB("", BR.email, BR.buttonSignInEnable)
    @get:Bindable var password: String by DB("", BR.password, BR.buttonSignInEnable)
    @get:Bindable var passwordConfirm: String by DB("", BR.passwordConfirm, BR.buttonSignInEnable)
    @get:Bindable var error: String by DB("", BR.error, BR.errorVisible)

    @get:Bindable val buttonSignInEnable: Boolean
        get() = email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()

    @get:Bindable val errorVisible: Boolean
        get() = error.isNotEmpty()

    private val errorMessageResLive = MutableLiveData<@androidx.annotation.StringRes Int>()
    private val throwableLive = MutableLiveData<Throwable>()
    private val openMainUserUid = MutableLiveData<String>()

    fun getErrorResLive(): LiveData<Int> {
        return errorMessageResLive
    }

    fun getThrowableLive(): LiveData<Throwable> {
        return throwableLive
    }

    fun openMainScreenByUserUid(): LiveData<String> {
        return openMainUserUid
    }

    fun signUp() {
        if (password.isNotEmpty() && password != passwordConfirm) {
            errorMessageResLive.value = R.string.error_passwords_match
            return
        }

        isProgress = true
        error = Config.EMPTY

        createUser.execute(CreateUserUseCase.Params(email, password),
            onSuccess = { firebaseUser ->
                isProgress = false
                openMainUserUid.value = firebaseUser.uid
            },
            onError = { throwable ->
                isProgress = false
                throwableLive.value = throwable
                if (throwable !is FirebaseAuthUserCollisionException) {
                    error = throwable.localizedMessage ?: Config.ERROR
                }
            }
        )
    }

    fun setErrorMessage(message: String) {
        error = message
    }
}