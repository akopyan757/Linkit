package com.example.linkit_app.ui.auth.splash

import androidx.lifecycle.MutableLiveData
import com.akopyan757.linkit_domain.usecase.auth.GetUserUseCase
import com.example.linkit_app.compose.BaseViewModel

class AuthSplashViewModel : BaseViewModel() {

    private val getUser: GetUserUseCase by injectUseCase()

    val buttonEnabled = MutableLiveData(false)
    val progressVisibility = MutableLiveData(false)
    val errorMessage = MutableLiveData("")

    fun onSplashFinished(onFinished: (userFound: Boolean) -> Unit) {
        getUser.execute(onSuccess = {
            onFinished.invoke(true)
        }, onError = {
            onFinished.invoke(false)
        })
    }
}