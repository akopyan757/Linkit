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

class ProfileUpdatePasswordViewModel: BaseViewModel(), KoinComponent {

    /** Databinding properties */
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

    /** Private properties */
    private var resErrorNotMatch: String = ""

    /** Models */
    private val authRepository: AuthRepository by inject()

    /** Requests */
    private val requestUpdatePassword = MutableLiveData<Pair<String, String>>()

    /** DataBinding methods */
    fun onUpdatePasswordButtonClicked() {
        if (password.isNotEmpty() && password != passwordConfirm) {
            error = resErrorNotMatch
            return
        }

        Log.i(TAG, "password=$password")

        requestUpdatePassword.value = Pair(oldPassword, password)
    }

    /** Responses */
    fun getSetPasswordResponseLive() = requestUpdatePassword.switchMap { (oldPassword, newPassword) ->
        requestConvertSimple<Unit> (
            method = { authRepository.updatePassword(oldPassword, newPassword) },
            onSuccess = { firebaseUser ->
                Log.i(TAG, "updatePassword: success: $firebaseUser")
            }, onError = { exception ->
                Log.w(TAG, "updatePassword: failure", exception)
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