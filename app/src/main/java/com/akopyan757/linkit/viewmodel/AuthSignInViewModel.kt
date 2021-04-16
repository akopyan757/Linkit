package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit_domain.usecase.auth.SignInWithEmailUserUseCase
import org.koin.core.KoinComponent

class AuthSignInViewModel: BaseViewModel(), KoinComponent {

    private val signInWithEmail: SignInWithEmailUserUseCase by injectUseCase()

    @get:Bindable var isProgress: Boolean by DB(false, BR.progress)
    @get:Bindable var email: String by DB("", BR.email, BR.buttonSignInEnable)
    @get:Bindable var password: String by DB("", BR.password, BR.buttonSignInEnable)
    @get:Bindable var error: String by DB("", BR.error, BR.errorVisible)

    @get:Bindable val buttonSignInEnable: Boolean
        get() = email.isNotEmpty() && password.isNotEmpty()

    @get:Bindable val errorVisible: Boolean
        get() = error.isNotEmpty()

    private val openMainUserUid = MutableLiveData<String>()
    private val throwableLive = MutableLiveData<Throwable>()

    fun openMainScreenByUserUid(): LiveData<String> {
        return openMainUserUid
    }

    fun getThrowableLive(): LiveData<Throwable> {
        return throwableLive
    }

    fun requestSignInWithEmail() {
        isProgress = true
        signInWithEmail.execute(SignInWithEmailUserUseCase.Params(email, password),
            onSuccess = { userEntity ->
                isProgress = false
                openMainUserUid.value = userEntity.uid
            }, onError = { throwable ->
                isProgress = false
                throwableLive.value = throwable
            })
    }

}