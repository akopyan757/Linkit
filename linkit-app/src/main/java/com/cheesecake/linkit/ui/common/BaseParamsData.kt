package com.cheesecake.linkit.ui.common

import com.cheesecake.linkit.ui.common.inputitem.IInputItem
import com.cheesecake.linkit.ui.common.inputitem.checkOptions.BaseCheckOption

abstract class BaseParamsData {

    abstract fun getInputItems(): List<IInputItem>

    open fun getValidInputErrorMessageList(delayCheck: Boolean): List<BaseCheckOption> {
        return getInputItems().flatMap { inputItem ->
            inputItem.getErrorCheckOptions(getInputItems(), delayCheck)
        }
    }

    fun isValidEnabled(): Boolean {
        return getInputItems().flatMap { inputItem ->
            inputItem.getErrorCheckOptions(getInputItems(), delayCheck = true)
        }.isEmpty()
    }
}