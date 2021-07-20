package com.example.linkit_app.ui.common.inputitem

import java.io.Serializable
import java.util.regex.Pattern


open class BaseCheckOption : Serializable {

    var mandatory: Boolean = false
    var minLength: Int = 0
    var maxLength: Int = 0
    var regex: String = ""
    var error: String = ""

    /**
     * Проверка поля на валидность
     *
     * @param delayCheck - если true, то производим проверку с учетом возможности ввести значение,
     * которое приведет к корректности проверки (актуально для проверки по регулярному выражению)
     */
    open fun isOk(currentInputItem: IInputItem, delayCheck: Boolean): Boolean {
        val value = currentInputItem.getInputItemValue()
        if (mandatory && value.isBlank()) {
            return false
        }
        if (!delayCheck && minLength != 0 && value.length < minLength) {
            return false
        }
        if (maxLength != 0 && value.length > maxLength) {
            return false
        }
        if (regex.isNotEmpty()) {
            if (delayCheck) {
                val matcher = Pattern.compile(regex).matcher(value)
                if (!matcher.matches() && !matcher.hitEnd()) {
                    return value.isEmpty() && !mandatory
                }
            } else if (!value.matches(Regex(regex))) {
                return value.isEmpty() && !mandatory
            }
        }
        return true
    }

    override fun toString(): String {
        return "BaseCheckOption(mandatory=$mandatory, minLength=$minLength, minLength=$maxLength, " +
                "regex=$regex, error=$error)"
    }
}