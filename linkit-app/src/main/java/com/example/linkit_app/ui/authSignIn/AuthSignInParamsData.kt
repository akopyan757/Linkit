package com.example.linkit_app.ui.authSignIn

import com.example.linkit_app.ui.common.inputitem.EmailInputItem
import com.example.linkit_app.ui.common.inputitem.IInputItem
import com.example.linkit_app.ui.common.inputitem.PasswordInputItem

class AuthSignInParamsData {

    var email: EmailInputItem? = null
    var password: PasswordInputItem? = null

    fun getInputItems(): List<IInputItem> {
        val inputItems = ArrayList<IInputItem>()
        email?.let(inputItems::add)
        password?.let(inputItems::add)
        return inputItems
    }

    fun setErrorState(state: Boolean) {
        email?.setError(state)
        password?.setError(state)
    }

    fun getParamsData() = AuthSignInRequest(
        email = email?.getInputItemValue() ?: "",
        password = password?.getInputItemValue() ?: ""
    )

    fun getValidInputErrorMessages(delayCheck: Boolean): String {
        return getInputItems().flatMap { inputItem ->
            inputItem.getErrorCheckOptions(delayCheck)
        }.joinToString("\n") { checkOption -> "* " + checkOption.error }
    }

    fun isValidEnabled(): Boolean {
        return getInputItems().flatMap { inputItem ->
            inputItem.getErrorCheckOptions(delayCheck = true)
        }.isEmpty()
    }
}