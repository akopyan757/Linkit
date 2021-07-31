package com.cheesecake.linkit.ui.auth.signIn

import com.cheesecake.linkit.ui.common.BaseParamsData
import com.cheesecake.linkit.ui.common.inputitem.EmailInputItem
import com.cheesecake.linkit.ui.common.inputitem.IInputItem
import com.cheesecake.linkit.ui.common.inputitem.PasswordInputItem

class AuthSignInParamsData: BaseParamsData() {

    var email: EmailInputItem? = null
    var password: PasswordInputItem? = null

    override fun getInputItems(): List<IInputItem> {
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

    fun getValidInputErrorMessage(delayCheck: Boolean): String {
        return super.getValidInputErrorMessageList(delayCheck).joinToString("\n") {
                checkOption -> "* " + checkOption.error
        }
    }
}