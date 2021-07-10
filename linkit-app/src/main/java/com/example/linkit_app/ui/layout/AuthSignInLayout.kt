package com.example.linkit_app.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.linkit_app.R
import com.example.linkit_app.ui.common.ButtonLayout
import com.example.linkit_app.ui.common.EmailInputItemLayout
import com.example.linkit_app.ui.common.PasswordInputItemLayout
import com.example.linkit_app.ui.theme.LinkitTheme
import com.example.linkit_app.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AuthSignInLayout() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }
        var errorState by remember { mutableStateOf(false) }
        var buttonEnabled by remember { mutableStateOf(false) }
        var progressVisible by remember { mutableStateOf(false) }

        val focusManager = LocalFocusManager.current

        Image(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "Home",
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .align(Alignment.Start)
        )

        Text(
            text = "Sign in with email",
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Bottom)
                .weight(1f)
        )

        EmailInputItemLayout(
            value = email,
            setTextChanged = {
                email = it
                errorState = false
                errorMessage = ""
                buttonEnabled = email.isNotEmpty() && password.isNotEmpty()
            },
            hint = "Email",
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            errorState = errorState,
            modifier = Modifier.padding(top = 16.dp)
        )

        PasswordInputItemLayout(
            value = password,
            setTextChanged = {
                password = it
                errorState = false
                errorMessage = ""
                buttonEnabled = email.isNotEmpty() && password.isNotEmpty()
            },
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            hint = "Password",
            error = errorMessage,
            errorState = errorState,
            modifier = Modifier.padding(top = 16.dp)
        )

        if (progressVisible) {
            CircularProgressIndicator(
                strokeWidth = 5.dp,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .wrapContentWidth()
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            text = "Forget password?",
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .weight(1f)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .wrapContentHeight(Alignment.CenterVertically)
        )

        ButtonLayout(
            text = "Sign in",
            enabled = buttonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Bottom)
        ) {
            errorMessage = ""
            errorState = false
            progressVisible = !progressVisible
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AuthSignInLayoutPreview() {
    LinkitTheme(darkTheme = false) {
        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(White, true)
        AuthSignInLayout()
    }
}