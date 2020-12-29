package com.akopyan757.linkit.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import org.koin.core.KoinComponent
import org.koin.core.inject

class ProfileSetPasswordViewModel: BaseViewModel(), KoinComponent {

    /** Databinding properties */
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

    /** Private properties */
    private var resErrorNotMatch: String = ""

    /** Models */
    private val authRepository: AuthRepository by inject()

    /** Requests */
    private val requestSetPassword = MutableLiveData<String>()

    /** DataBinding methods */
    fun onSetPasswordButtonClicked() {
        if (password.isNotEmpty() && password != passwordConfirm) {
            error = resErrorNotMatch
            return
        }

        Log.i(TAG, "password=$password")

        requestSetPassword.value = password
    }

    /** Responses */
    fun getSetPasswordResponseLive() = requestSetPassword.switchMap { password ->
        requestConvert<FirebaseUser, String> (
            method = { authRepository.linkPasswordToAccount(password) },
            onSuccess = { firebaseUser ->
                Log.i(TAG, "linkPasswordToAccount: success: $firebaseUser")
                firebaseUser.uid
            }, onError = { exception ->
                Log.w(TAG, "linkPasswordToAccount: failure", exception)
                error = exception.localizedMessage ?: "Error"
            }
        )
    }

    /** Public method */
    fun initRes(errorNotMatch: String) {
        resErrorNotMatch = errorNotMatch
    }

    companion object {
        private const val TAG = "PROFILE_SET_PASSWORD_VM"
    }
}