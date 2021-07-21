package com.example.linkit_app.ui.auth.updatePassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import com.example.linkit_app.ui.auth.AuthLayout
import com.example.linkit_app.ui.theme.LinkitTheme
import com.example.linkit_app.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AuthUpdatePasswordLayout(viewModel: AuthUpdatePasswordViewModel = AuthUpdatePasswordViewModel()) {
    val params by viewModel.params.observeAsState()
    val buttonEnabled by viewModel.buttonEnabled.observeAsState(false)
    val progressVisible by viewModel.progressVisibility.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState("")

    AuthLayout(
        params = params,
        titleH1 = "Update password",
        progressVisible = progressVisible,
        errorMessage = errorMessage,
        buttonName = "Send",
        buttonEnabled = buttonEnabled,
        onButtonClicked = viewModel::onSendClicked
    )
}

@Composable
@Preview(showBackground = true)
fun AuthUpdatePasswordLayoutPreview() {
    LinkitTheme(darkTheme = false) {
        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(White, true)
        AuthUpdatePasswordLayout()
    }
}