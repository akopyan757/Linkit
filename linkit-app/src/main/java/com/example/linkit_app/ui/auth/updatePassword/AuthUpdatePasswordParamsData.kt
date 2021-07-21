package com.example.linkit_app.ui.auth.updatePassword

import com.example.linkit_app.ui.common.BaseParamsData
import com.example.linkit_app.ui.common.inputitem.IInputItem
import com.example.linkit_app.ui.common.inputitem.PasswordInputItem

class AuthUpdatePasswordParamsData: BaseParamsData() {

    var oldPassword: PasswordInputItem? = null
    var newPassword: PasswordInputItem? = null
    var passwordConfirm: PasswordInputItem? = null

    override fun getInputItems(): List<IInputItem> {
        val inputItems = ArrayList<IInputItem>()
        oldPassword?.let(inputItems::add)
        newPassword?.let(inputItems::add)
        passwordConfirm?.let(inputItems::add)
        return inputItems
    }

    fun setErrorState(state: Boolean) {
        oldPassword?.setError(state)
        newPassword?.setError(state)
        passwordConfirm?.setError(state)
    }

    fun getParamsData() = AuthUpdatePasswordRequest(
        oldPassword = oldPassword?.getInputItemValue() ?: "",
        newPassword = newPassword?.getInputItemValue() ?: "",
        passwordConfirm = passwordConfirm?.getInputItemValue() ?: ""
    )

    fun getValidInputErrorMessage(delayCheck: Boolean): String {
        return super.getValidInputErrorMessageList(delayCheck).joinToString("\n") {
            checkOption -> "* " + checkOption.error
        }
    }
}