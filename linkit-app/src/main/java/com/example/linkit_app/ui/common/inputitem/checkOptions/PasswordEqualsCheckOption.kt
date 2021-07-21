package com.example.linkit_app.ui.common.inputitem.checkOptions

import com.example.linkit_app.ui.common.inputitem.IInputItem

class PasswordEqualsCheckOption : BaseCheckOption() {

    var equalsItemId: String = ""

    override fun isOk(currentInputItem: IInputItem, inputItems: List<IInputItem>, delayCheck: Boolean): Boolean {
        val result = super.isOk(currentInputItem, inputItems, delayCheck)
        if (result) {
            val equalsItem = inputItems.find { it.getInputItemId() == equalsItemId }?.getInputItemValue()
            val value = currentInputItem.getInputItemValue()
            if (!equalsItem.isNullOrEmpty()
                    && equalsItem != value
                    && (value.length >= equalsItem.length || (value.length < equalsItem.length && equalsItem.substring(0, value.length) != value))) {
                return false
            }
            if (!equalsItem.isNullOrEmpty() && equalsItem != value && !delayCheck) {
                return false
            }
        }
        return result
    }
}