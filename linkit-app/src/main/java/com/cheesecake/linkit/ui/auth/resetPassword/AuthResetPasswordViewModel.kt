package com.cheesecake.linkit.ui.auth.resetPassword

import androidx.lifecycle.MutableLiveData
import com.akopyan757.linkit_domain.usecase.auth.ResetPasswordUseCase
import com.cheesecake.linkit.compose.BaseViewModel
import com.cheesecake.linkit.ui.common.inputitem.EmailInputItem
import com.cheesecake.linkit.ui.common.inputitem.checkOptions.BaseCheckOption

class AuthResetPasswordViewModel : BaseViewModel() {

    private val resetPassword: ResetPasswordUseCase by injectUseCase()

    val params = MutableLiveData<AuthResetPasswordParamsData>()
    val buttonEnabled = MutableLiveData(false)
    val progressVisibility = MutableLiveData(false)
    val errorMessage = MutableLiveData("")

    init {
        initInputData()
    }

    fun onResetPasswordClicked() {
        val params = params.value ?: return
        if (!params.isValidEnabled()) {
            updateErrorState(params, delayCheck = false)
        } else {
            progressVisibility.value = true
            val request = params.getParamsData().let { data ->
                ResetPasswordUseCase.Params(data.email)
            }
            resetPassword.execute(
                params = request,
                onSuccess = {
                    progressVisibility.value = false
                    params.setErrorState(false)
                }, onError = {
                    errorMessage.value = "* ${it.localizedMessage}"
                    progressVisibility.value = false
                    params.setErrorState(true)
                }
            )
        }
    }

    private fun initInputData() {
        val data = AuthResetPasswordParamsData()
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
        params.postValue(data)
    }

    private fun updateButtonEnableState() {
        buttonEnabled.value = params.value?.isValidEnabled()
    }

    private fun updateErrorState(param: AuthResetPasswordParamsData, delayCheck: Boolean) {
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
    }
}