package com.example.linkit_app.ui.auth.signUp

import com.example.linkit_app.ui.common.BaseParamsData
import com.example.linkit_app.ui.common.inputitem.EmailInputItem
import com.example.linkit_app.ui.common.inputitem.IInputItem
import com.example.linkit_app.ui.common.inputitem.PasswordInputItem

class AuthSignUpParamsData: BaseParamsData() {

    var email: EmailInputItem? = null
    var password: PasswordInputItem? = null
    var passwordConfirm: PasswordInputItem? = null

    override fun getInputItems(): List<IInputItem> {
        val inputItems = ArrayList<IInputItem>()
        email?.let(inputItems::add)
        password?.let(inputItems::add)
        passwordConfirm?.let(inputItems::add)
        return inputItems
    }

    fun setErrorState(state: Boolean) {
        email?.setError(state)
        password?.setError(state)
        passwordConfirm?.setError(state)
    }

    fun getParamsData() = AuthSignUpRequest(
        email = email?.getInputItemValue() ?: "",
        password = password?.getInputItemValue() ?: "",
        passwordConfirm = passwordConfirm?.getInputItemValue() ?: ""
    )

    fun getValidInputErrorMessage(delayCheck: Boolean): String {
        return super.getValidInputErrorMessageList(delayCheck).joinToString("\n") {
                checkOption -> "* " + checkOption.error
        }
    }
}