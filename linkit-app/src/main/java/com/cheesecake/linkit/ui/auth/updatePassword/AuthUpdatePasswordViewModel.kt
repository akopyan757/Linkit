package com.cheesecake.linkit.ui.auth.updatePassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cheesecake.linkit.ui.common.inputitem.checkOptions.BaseCheckOption
import com.cheesecake.linkit.ui.common.inputitem.PasswordInputItem
import com.cheesecake.linkit.ui.common.inputitem.checkOptions.PasswordEqualsCheckOption

class AuthUpdatePasswordViewModel : ViewModel() {

    val params = MutableLiveData<AuthUpdatePasswordParamsData>()
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
        val data = AuthUpdatePasswordParamsData()
        data.oldPassword = PasswordInputItem(INPUT_TYPE_OLD_PASSWORD).apply {
            label = "Old password"
            onTextChanged = {
                updateButtonEnableState()
                updateErrorState(data, delayCheck = true)
            }
            checkOptions.apply {
                add(BaseCheckOption().apply {
                    mandatory = true
                    error = "Old password is mandatory field"
                })
            }
        }
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

    private fun updateErrorState(param: AuthUpdatePasswordParamsData, delayCheck: Boolean) {
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
        private const val INPUT_TYPE_OLD_PASSWORD = "INPUT_TYPE_OLD_PASSWORD"
        private const val INPUT_TYPE_NEW_PASSWORD = "INPUT_TYPE_NEW_PASSWORD"
        private const val INPUT_TYPE_PASSWORD_CONFIRM = "INPUT_TYPE_PASSWORD_CONFIRM"
    }
}