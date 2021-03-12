package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import org.koin.core.KoinComponent
import org.koin.core.inject

class AuthSignUpViewModel: BaseViewModel(), KoinComponent {

    private val authRepository: AuthRepository by inject()

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

    fun requestSignUp(): LiveData<ResponseState<String>> {
        if (password.isNotEmpty() && password != passwordConfirm)
            return emptyLiveRequest()

        return requestConvert(
            request = authRepository.createUser(email, password),
            onLoading = {
                isProgress = true
                error = Config.EMPTY
            }, onSuccess = { firebaseUser ->
                isProgress = false
                return@requestConvert firebaseUser.uid
            },
            onError = { exception ->
                isProgress = false
                if (exception !is FirebaseAuthUserCollisionException) {
                    error = exception.localizedMessage ?: Config.ERROR
                }
            }
        )
    }

    fun setErrorMessage(message: String) {
        error = message
    }
}