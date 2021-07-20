package com.example.linkit_app.ui.authSignUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.linkit_app.R
import com.example.linkit_app.ui.common.ButtonLayout
import com.example.linkit_app.ui.theme.Error
import com.example.linkit_app.ui.theme.LinkitTheme
import com.example.linkit_app.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AuthSignUpLayout(viewModel: AuthSignUpViewModel = AuthSignUpViewModel()) {
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val params by viewModel.params.observeAsState()
        val buttonEnabled by viewModel.buttonEnabled.observeAsState(false)
        val progressVisible by viewModel.progressVisibility.observeAsState(false)
        val errorMessage by viewModel.errorMessage.observeAsState("")

        Image(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "Home",
            modifier = Modifier.width(24.dp).height(24.dp).align(Alignment.Start)
        )

        Text(
            text = "Sign up with email",
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Bottom)
                .weight(1f)
        )

        params?.getInputItems()?.forEach { inputItem ->
            Spacer(modifier = Modifier.height(16.dp))
            inputItem.getInputLayout()
        }

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.overline,
            color = Error,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, start = 16.dp)
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

        ButtonLayout(
            text = "Sign up",
            enabled = buttonEnabled,
            modifier = Modifier.fillMaxWidth().wrapContentHeight(Alignment.Bottom)
        ) {
            viewModel.onSignUpClicked()
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