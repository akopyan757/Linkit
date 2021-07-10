package com.example.linkit_app.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.linkit_app.R
import com.example.linkit_app.ui.theme.*

@Composable
fun InputItemLayout(
    value: String,
    setTextChanged: (String) -> Unit,
    hint: String,
    error: String = "",
    errorState: Boolean,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable () -> Unit = {},
    modifier: Modifier
) {

    Column(
        modifier = modifier
    ) {
        TextField(
            value = value,
            onValueChange = setTextChanged,
            label = { Text(text = hint) },
            maxLines = 1,
            shape = RoundedCornerShape(16.dp),
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Surface,
                cursorColor = Black,
                errorIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = Grey,
                unfocusedLabelColor = Grey
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            isError = errorState
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                style = MaterialTheme.typography.overline,
                color = Error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp)
            )
        }
    }
}

@Composable
fun EmailInputItemLayout(
    value: String,
    setTextChanged: (String) -> Unit,
    hint: String,
    error: String = "",
    errorState: Boolean,
    keyboardActions: KeyboardActions = KeyboardActions(),
    modifier: Modifier
) {
    InputItemLayout(
        value = value,
        setTextChanged = setTextChanged,
        hint = hint,
        error = error,
        errorState = errorState,
        keyboardActions = keyboardActions,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = hint,
                tint = if (errorState) Error else Grey,
            ) },
        modifier = modifier
    )
}

@Composable
fun PasswordInputItemLayout(
    value: String,
    setTextChanged: (String) -> Unit,
    hint: String,
    error: String = "",
    errorState: Boolean,
    keyboardActions: KeyboardActions = KeyboardActions(),
    modifier: Modifier
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    InputItemLayout(
        value = value,
        setTextChanged = setTextChanged,
        hint = hint,
        error = error,
        errorState = errorState,
        keyboardActions = keyboardActions,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisibility) R.drawable.ic_password_eye else R.drawable.ic_password_eye_off
            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }, modifier = Modifier.width(24.dp).height(24.dp)) {
                Icon(
                    painter = painterResource(image),
                    contentDescription = hint,
                    tint = if (errorState) Error else Grey,
                )
            }
        },
        modifier = modifier
    )
}

@Composable
@Preview(showBackground = true, name = "Email Input Preview")
fun EmailInputItemLayoutPreview() {
    LinkitTheme {
        val text = remember { mutableStateOf("akopyan@gmail.com") }
        val error = remember { mutableStateOf("") }
        val errorState = remember { mutableStateOf(false) }
        EmailInputItemLayout(
            value = text.value,
            setTextChanged = { value ->
                text.value = value
                error.value = if (value.isEmpty()) "Error" else ""
                errorState.value = value.isEmpty()
            },
            hint = "Hint",
            error = error.value,
            errorState = errorState.value,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(24.dp)
        )
    }
}

@Composable
@Preview(showBackground = true, name = "Password Input Preview")
fun PasswordInputItemErrorLayoutPreview() {
    LinkitTheme {
        val text = remember { mutableStateOf("ABCDEF1234") }
        val error = remember { mutableStateOf("") }
        val errorState = remember { mutableStateOf(false) }
        PasswordInputItemLayout(
            value = text.value,
            setTextChanged = { value ->
                text.value = value
                error.value = if (value.isEmpty()) "Error" else ""
                errorState.value = value.isEmpty()
            },
            hint = "Hint",
            error = error.value,
            errorState = errorState.value,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(24.dp)
        )
    }
}