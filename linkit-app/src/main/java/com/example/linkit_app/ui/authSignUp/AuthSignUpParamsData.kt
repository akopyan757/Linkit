package com.example.linkit_app.ui.authSignUp

import com.example.linkit_app.ui.common.inputitem.EmailInputItem
import com.example.linkit_app.ui.common.inputitem.IInputItem
import com.example.linkit_app.ui.common.inputitem.PasswordInputItem

class AuthSignUpParamsData {

    var email: EmailInputItem? = null
    var password: PasswordInputItem? = null
    var passwordConfirm: PasswordInputItem? = null

    fun getInputItems(): List<IInputItem> {
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
        passwordConfirm = password?.getInputItemValue() ?: ""
    )

    fun getValidInputErrorMessages(delayCheck: Boolean): String {
        return getInputItems().flatMap { inputItem ->
            inputItem.getErrorCheckOptions(getInputItems(), delayCheck)
        }.joinToString("\n") { checkOption -> "* " + checkOption.error }
    }

    fun isValidEnabled(): Boolean {
        return getInputItems().flatMap { inputItem ->
            inputItem.getErrorCheckOptions(getInputItems(), delayCheck = true)
        }.isEmpty()
    }
}