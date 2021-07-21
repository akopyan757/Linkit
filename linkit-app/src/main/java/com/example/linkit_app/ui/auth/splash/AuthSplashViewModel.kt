package com.example.linkit_app.ui.auth.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.linkit_app.ui.common.inputitem.checkOptions.BaseCheckOption
import com.example.linkit_app.ui.common.inputitem.EmailInputItem
import com.example.linkit_app.ui.common.inputitem.PasswordInputItem
import com.example.linkit_app.ui.common.inputitem.checkOptions.PasswordEqualsCheckOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthSplashViewModel : ViewModel() {

    val buttonEnabled = MutableLiveData(false)
    val progressVisibility = MutableLiveData(false)
    val errorMessage = MutableLiveData("")

    fun onSplashFinished(onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            onFinished.invoke()
        }
    }

    companion object {
        private const val INPUT_TYPE_EMAIL = "INPUT_TYPE_EMAIL"
        private const val INPUT_TYPE_PASSWORD = "INPUT_TYPE_PASSWORD"
        private const val INPUT_TYPE_PASSWORD_CONFIRM = "INPUT_TYPE_PASSWORD_CONFIRM"
    }
}