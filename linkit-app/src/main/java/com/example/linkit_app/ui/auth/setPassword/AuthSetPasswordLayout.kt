package com.example.linkit_app.ui.auth.setPassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.linkit_app.ui.auth.AuthLayout
import com.example.linkit_app.ui.theme.LinkitTheme
import com.example.linkit_app.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AuthSetPasswordLayout(navController: NavHostController) {
    val viewModel: AuthSetPasswordViewModel = viewModel()

    val params by viewModel.params.observeAsState()
    val buttonEnabled by viewModel.buttonEnabled.observeAsState(false)
    val progressVisible by viewModel.progressVisibility.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState("")

    AuthLayout<AuthSetPasswordParamsData>(
        params = params,
        titleH1 = "Password setup",
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
        val navController = rememberNavController()
        systemUiController.setStatusBarColor(White, true)
        AuthSetPasswordLayout(navController)
    }
}