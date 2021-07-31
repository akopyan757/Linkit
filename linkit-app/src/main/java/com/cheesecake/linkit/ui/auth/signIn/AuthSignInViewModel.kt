package com.cheesecake.linkit.ui.auth.signIn

import androidx.lifecycle.MutableLiveData
import com.akopyan757.linkit_domain.usecase.auth.SignInWithEmailUserUseCase
import com.cheesecake.linkit.compose.BaseViewModel
import com.cheesecake.linkit.ui.common.inputitem.EmailInputItem
import com.cheesecake.linkit.ui.common.inputitem.PasswordInputItem
import com.cheesecake.linkit.ui.common.inputitem.checkOptions.BaseCheckOption

class AuthSignInViewModel : BaseViewModel() {

    private val signInWithEmail: SignInWithEmailUserUseCase by injectUseCase()

    val params = MutableLiveData<AuthSignInParamsData>()
    val buttonEnabled = MutableLiveData(false)
    val progressVisibility = MutableLiveData(false)
    val errorMessage = MutableLiveData("")

    init {
        initInputData()
    }

    fun onSignInClicked(onMainStart: (uid: String) -> Unit) {
        val params = params.value ?: return
        if (!params.isValidEnabled()) {
            updateErrorState(params, delayCheck = false)
        } else {
            progressVisibility.value = true
            val request = params.getParamsData().let { data ->
                SignInWithEmailUserUseCase.Params(data.email, data.password)
            }
            signInWithEmail.execute(
                params = request,
                onSuccess = { entity ->
                    onMainStart.invoke(entity.uid)
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