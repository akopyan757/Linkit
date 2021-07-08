package com.example.linkit_app.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.linkit_app.R
import com.example.linkit_app.ui.common.ButtonLayout
import com.example.linkit_app.ui.common.InputItemLayout
import com.example.linkit_app.ui.theme.LinkitTheme
import com.example.linkit_app.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AuthSignUpLayout() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordConfirm by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }
        var errorState by remember { mutableStateOf(false) }
        var buttonEnabled by remember { mutableStateOf(false) }
        var progressVisible by remember { mutableStateOf(true) }

        Image(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "Home",
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .align(Alignment.Start)
        )

        Text(
            text = "Sign up with email",
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Bottom)
                .weight(1f)
        )

        InputItemLayout(
            value = email,
            setTextChanged = {
                email = it
                errorState = false
                errorMessage = ""
                buttonEnabled = email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()
            }, hint = "Email",
            errorState = errorState,
            modifier = Modifier.padding(top = 16.dp)
        )

        InputItemLayout(
            value = password,
            setTextChanged = {
                password = it
                errorState = false
                errorMessage = ""
                buttonEnabled = email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()
            },
            hint = "Password",
            errorState = errorState,
            modifier = Modifier.padding(top = 16.dp)
        )

        InputItemLayout(
            value = passwordConfirm,
            setTextChanged = {
                passwordConfirm = it
                errorState = false
                errorMessage = ""
                buttonEnabled = email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty()
            },
            hint = "Confirm Password",
            error = errorMessage,
            errorState = errorState,
            modifier = Modifier.padding(top = 16.dp)
        )

        if (progressVisible) {
            CircularProgressIndicator(
                strokeWidth = 5.dp,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(Alignment.Bottom)
                    .wrapContentWidth()
            )
        }


        ButtonLayout(
            text = "Sign up with email",
            enabled = buttonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .wrapContentHeight(Alignment.Bottom)
        ) {
            if (passwordConfirm != password) {
                errorMessage = "Password not matched"
                errorState = true
            } else {
                errorMessage = ""
                errorState = false
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AuthSignUpLayoutPreview() {
    LinkitTheme(darkTheme = false) {
        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(White, true)
        AuthSignUpLayout()
    }
}