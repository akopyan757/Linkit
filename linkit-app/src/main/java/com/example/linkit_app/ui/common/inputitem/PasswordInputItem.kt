package com.example.linkit_app.ui.common.inputitem

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.example.linkit_app.R
import com.example.linkit_app.ui.common.inputitem.IInputItem

class PasswordInputItem: IInputItem() {

    override val keyboardType: KeyboardType = KeyboardType.Password

    private val passwordVisibility = MutableLiveData(false)

    @Composable
    override fun getVisualTransformation(): VisualTransformation {
        val iconVisibility by passwordVisibility.observeAsState(false)
        return if (iconVisibility) VisualTransformation.None else PasswordVisualTransformation()
    }

    @Composable
    override fun GetTrailingIcon() {
        val iconVisibility by passwordVisibility.observeAsState(false)
        val image = if (iconVisibility) R.drawable.ic_password_eye else R.drawable.ic_password_eye_off
        val state by getErrorState().observeAsState(false)

        IconButton(
            modifier = Modifier
                .width(24.dp)
                .height(24.dp),
            onClick = { passwordVisibility.value = iconVisibility.not() }
        ) {
            Icon(
                painter = painterResource(image),
                contentDescription = label,
                tint = if (state) errorColor else tintColor,
            )
        }
    }
}