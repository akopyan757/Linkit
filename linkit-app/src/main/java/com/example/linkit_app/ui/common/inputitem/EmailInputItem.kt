package com.example.linkit_app.ui.common.inputitem

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.input.KeyboardType

class EmailInputItem: IInputItem() {

    override val keyboardType: KeyboardType = KeyboardType.Email

    init {
        checkOptions.add(BaseCheckOption().apply {
            regex = REGEX
            error = "Email is invalid"
        })
    }

    @Composable
    override fun GetTrailingIcon() {
        val state by getErrorState().observeAsState(false)
        Icon(
            imageVector = Icons.Outlined.Email,
            contentDescription = label,
            tint = if (state) errorColor else tintColor,
        )
    }

    companion object {
        private const val REGEX = "^[\\w!#\$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"
    }
}