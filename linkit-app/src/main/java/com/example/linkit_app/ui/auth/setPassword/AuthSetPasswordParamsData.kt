package com.example.linkit_app.ui.auth.setPassword

import com.example.linkit_app.ui.common.BaseParamsData
import com.example.linkit_app.ui.common.inputitem.IInputItem
import com.example.linkit_app.ui.common.inputitem.PasswordInputItem

class AuthSetPasswordParamsData: BaseParamsData() {

    var newPassword: PasswordInputItem? = null
    var passwordConfirm: PasswordInputItem? = null

    override fun getInputItems(): List<IInputItem> {
        val inputItems = ArrayList<IInputItem>()
        newPassword?.let(inputItems::add)
        passwordConfirm?.let(inputItems::add)
        return inputItems
    }

    fun setErrorState(state: Boolean) {
        newPassword?.setError(state)
        passwordConfirm?.setError(state)
    }

    fun getParamsData() = AuthSetPasswordRequest(
        newPassword = newPassword?.getInputItemValue() ?: "",
        passwordConfirm = passwordConfirm?.getInputItemValue() ?: ""
    )

    fun getValidInputErrorMessage(delayCheck: Boolean): String {
        return super.getValidInputErrorMessageList(delayCheck).joinToString("\n") {
            checkOption -> "* " + checkOption.error
        }
    }
}