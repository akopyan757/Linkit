package com.akopyan757.linkit.viewmodel

import android.content.Intent
import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.repository.AuthRepository
import com.akopyan757.linkit.view.service.IAuthorizationService
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.Exception

class AuthSignInViewModel: BaseViewModel(), KoinComponent {

    /** Databinding properties */
    @get:Bindable
    var email: String by DelegatedBindable("", BR.email, BR.buttonSignInEnable)

    @get:Bindable
    var password: String by DelegatedBindable("", BR.password, BR.buttonSignInEnable)

    @get:Bindable
    val buttonSignInEnable: Boolean
        get() = email.isNotEmpty() && password.isNotEmpty()

    @get:Bindable
    var error: String by DelegatedBindable("", BR.error, BR.errorVisible)

    @get:Bindable
    val errorVisible: Boolean
        get() = error.isNotEmpty()

    /** Models */
    private val authService: IAuthorizationService by inject()
    private val authRepository: AuthRepository by inject()

    /** Requests */
    private val requestSignInWithEmail = MutableLiveData<Pair<String, String>>()
    private val requestSignInResult = MutableLiveData<Intent>()

    /** DataBinding methods */
    fun onSignInButtonClicked() {
        requestSignInWithEmail.value = Pair(email, password)
    }

    /** Responses */
    fun getSignInResponseLive() = requestSignInWithEmail.switchMap { (email, password) ->
        requestConvert<FirebaseUser, String> (
            method = { authRepository.signInWithEmail(email, password) },
            onSuccess = { firebaseUser -> firebaseUser.uid },
            onError = { exception -> error = exception.localizedMessage ?: "Error" }
        )
    }

    fun getSignInServiceResponseLive() = requestSignInResult.switchMap { data ->
        requestConvert<FirebaseUser, String>(
            method = { authRepository.signInWithData(data) },
            onSuccess = { firebaseUser -> firebaseUser.uid },
            onError = { exception ->
                Log.i("TAG", "EXCEPTION", exception)
                error = exception.localizedMessage ?: "Error"
            }
        )
    }

    /** Public methods */
    fun getSignInIntent() = authService.getSignInIntent()

    fun onHuaweiDataResult(data: Intent?) {
        requestSignInResult.value = data
    }
}