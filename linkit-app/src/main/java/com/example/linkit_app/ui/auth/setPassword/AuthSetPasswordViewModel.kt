package com.example.linkit_app.ui.auth.setPassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.linkit_app.compose.BaseViewModel
import com.example.linkit_app.ui.common.inputitem.checkOptions.BaseCheckOption
import com.example.linkit_app.ui.common.inputitem.PasswordInputItem
import com.example.linkit_app.ui.common.inputitem.checkOptions.PasswordEqualsCheckOption

class AuthSetPasswordViewModel : BaseViewModel() {

    val params = MutableLiveData<AuthSetPasswordParamsData>()
    val buttonEnabled = MutableLiveData(false)
    val progressVisibility = MutableLiveData(false)
    val errorMessage = MutableLiveData("")

    init {
        initInputData()
    }

    fun onSendClicked() {
        val data = params.value ?: return
        updateErrorState(data, delayCheck = false)
        progressVisibility.value = progressVisibility.value?.not()
    }

    private fun initInputData() {
        val data = AuthSetPasswordParamsData()
        data.newPassword = PasswordInputItem(INPUT_TYPE_NEW_PASSWORD).apply {
            label = "New password"
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
                    equalsItemId = INPUT_TYPE_NEW_PASSWORD
                    error = "New password mismatch"
                })
            }
        }
        params.postValue(data)
    }

    private fun updateButtonEnableState() {
        buttonEnabled.value = params.value?.isValidEnabled()
    }

    private fun updateErrorState(param: AuthSetPasswordParamsData, delayCheck: Boolean) {
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
        private const val INPUT_TYPE_NEW_PASSWORD = "INPUT_TYPE_NEW_PASSWORD"
        private const val INPUT_TYPE_PASSWORD_CONFIRM = "INPUT_TYPE_PASSWORD_CONFIRM"
    }
}