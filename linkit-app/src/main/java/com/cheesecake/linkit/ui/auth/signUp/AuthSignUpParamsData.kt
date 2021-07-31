package com.cheesecake.linkit.ui.auth.signUp

import com.cheesecake.linkit.ui.common.BaseParamsData
import com.cheesecake.linkit.ui.common.inputitem.EmailInputItem
import com.cheesecake.linkit.ui.common.inputitem.IInputItem
import com.cheesecake.linkit.ui.common.inputitem.PasswordInputItem

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