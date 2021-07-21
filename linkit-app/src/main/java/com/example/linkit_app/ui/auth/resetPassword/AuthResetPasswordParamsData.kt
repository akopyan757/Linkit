package com.example.linkit_app.ui.auth.resetPassword

import com.example.linkit_app.ui.common.BaseParamsData
import com.example.linkit_app.ui.common.inputitem.EmailInputItem
import com.example.linkit_app.ui.common.inputitem.IInputItem

class AuthResetPasswordParamsData: BaseParamsData() {

    var email: EmailInputItem? = null

    override fun getInputItems(): List<IInputItem> {
        val inputItems = ArrayList<IInputItem>()
        email?.let(inputItems::add)
        return inputItems
    }

    fun setErrorState(state: Boolean) {
        email?.setError(state)
    }

    fun getParamsData() = AuthResetPasswordRequest(
        email = email?.getInputItemValue() ?: ""
    )

    fun getValidInputErrorMessage(delayCheck: Boolean): String {
        return super.getValidInputErrorMessageList(delayCheck).joinToString("\n") {
                checkOption -> "* " + checkOption.error
        }
    }
}