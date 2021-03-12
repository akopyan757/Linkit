package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import org.koin.core.KoinComponent
import org.koin.core.inject

class ProfileSetPasswordViewModel: BaseViewModel(), KoinComponent {

    private val authRepository: AuthRepository by inject()

    /** Databinding properties */
    @get:Bindable
    var isProgress: Boolean by DelegatedBindable(false, BR.progress)

    @get:Bindable
    var password: String by DelegatedBindable("", BR.password, BR.buttonSignInEnable)

    @get:Bindable
    var passwordConfirm: String by DelegatedBindable("", BR.passwordConfirm, BR.buttonSignInEnable)

    @get:Bindable
    val buttonSignInEnable: Boolean
        get() = password.isNotEmpty() && passwordConfirm.isNotEmpty()

    @get:Bindable
    var error: String by DelegatedBindable("", BR.error, BR.errorVisible)

    @get:Bindable
    val errorVisible: Boolean
        get() = error.isNotEmpty()


    fun getSetPasswordResponseLive(): LiveData<ResponseState<FirebaseUser>> {
        if (password.isNotEmpty() && password != passwordConfirm) {
            return emptyLiveRequest()
        }

        return requestConvert(
            request = authRepository.linkPasswordToAccount(password),
            onLoading = { isProgress = true },
            onSuccess = { firebaseUser ->
                isProgress = false
                return@requestConvert firebaseUser
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
        private const val TAG = "PROFILE_SET_PASSWORD_VM"
    }
}