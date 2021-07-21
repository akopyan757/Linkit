package com.example.linkit_app.ui.authSignUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.linkit_app.ui.common.inputitem.checkOptions.BaseCheckOption
import com.example.linkit_app.ui.common.inputitem.EmailInputItem
import com.example.linkit_app.ui.common.inputitem.PasswordInputItem
import com.example.linkit_app.ui.common.inputitem.checkOptions.PasswordEqualsCheckOption

class AuthSignUpViewModel : ViewModel() {

    val params = MutableLiveData<AuthSignUpParamsData>()
    val buttonEnabled = MutableLiveData(false)
    val progressVisibility = MutableLiveData(false)
    val errorMessage = MutableLiveData("")

    init {
        initInputData()
    }

    fun onSignUpClicked() {
        val data = params.value ?: return
        updateErrorState(data, delayCheck = false)
        progressVisibility.value = progressVisibility.value?.not()
    }

    private fun initInputData() {
        val data = AuthSignUpParamsData()
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
        data.passwordConfirm = PasswordInputItem(INPUT_TYPE_PASSWORD_CONFIRM).apply {
            label = "Confirm password"
            onTextChanged = {
                updateButtonEnableState()
                updateErrorState(data, delayCheck = true)
            }
            checkOptions.apply {
                add(BaseCheckOption().apply {
                    mandatory = true
                    error = "Confirm password is mandatory field"
                })
                add(BaseCheckOption().apply {
                    minLength = 8
                    error = "Confirm password cannot be less than $minLength characters"
                })
                add(PasswordEqualsCheckOption().apply {
                    equalsItemId = INPUT_TYPE_PASSWORD
                    error = "Password mismatch"
                })
            }
        }
        params.postValue(data)
    }

    private fun updateButtonEnableState() {
        buttonEnabled.value = params.value?.isValidEnabled()
    }

    private fun updateErrorState(param: AuthSignUpParamsData, delayCheck: Boolean) {
        val message = param.getValidInputErrorMessages(delayCheck)
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
        private const val INPUT_TYPE_PASSWORD_CONFIRM = "INPUT_TYPE_PASSWORD_CONFIRM"
    }
}