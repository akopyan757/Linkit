package com.example.linkit_app.ui.auth.signIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.linkit_app.ui.common.inputitem.checkOptions.BaseCheckOption
import com.example.linkit_app.ui.common.inputitem.EmailInputItem
import com.example.linkit_app.ui.common.inputitem.PasswordInputItem

class AuthSignInViewModel : ViewModel() {

    val params = MutableLiveData<AuthSignInParamsData>()
    val buttonEnabled = MutableLiveData(false)
    val progressVisibility = MutableLiveData(false)
    val errorMessage = MutableLiveData("")

    init {
        initInputData()
    }

    fun onSignInClicked() {
        val data = params.value ?: return
        updateErrorState(data, delayCheck = false)
        progressVisibility.value = progressVisibility.value?.not()
    }

    private fun initInputData() {
        val data = AuthSignInParamsData()
        data.email = EmailInputItem(INPUT_TYPE_EMAIL).apply {
            label = "Email"
            onTextChanged = {
                updateButtonEnableState()
                updateErrorState(data, delayCheck = true)
            }
            checkOptions.apply {
                add(BaseCheckOption().apply {
                    mandatory = true
                    error = "Email is mandatory field"
                })
            }
        }
        data.password = PasswordInputItem(INPUT_TYPE_PASSWORD).apply {
            label = "Password"
            onTextChanged = {
                updateButtonEnableState()
                updateErrorState(data, delayCheck = true)
            }
            checkOptions.apply {
                add(BaseCheckOption().apply {
                    mandatory = true
                    error = "Password is mandatory field"
                })
                add(BaseCheckOption().apply {
                    minLength = 8
                    error = "Password cannot be less than $minLength characters"
                })
            }
        }
        params.postValue(data)
    }

    private fun updateButtonEnableState() {
        buttonEnabled.value = params.value?.isValidEnabled()
    }

    private fun updateErrorState(param: AuthSignInParamsData, delayCheck: Boolean) {
        val message = param.getValidInputErrorMessage(delayCheck)
        if (message.isEmpty()) {
            errorMessage.postValue("")
            params.value?.setErrorState(false)
        } else {
            errorMessage.postValue(message)
            params.value?.setErrorState(true)
        }
    }

    companion object {
        private const val INPUT_TYPE_EMAIL = "INPUT_TYPE_EMAIL"
        private const val INPUT_TYPE_PASSWORD = "INPUT_TYPE_PASSWORD"
    }
}