package com.example.linkit_app.ui.auth.signUp

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
fun AuthSignUpLayout(navController: NavHostController) {
    val viewModel: AuthSignUpViewModel = viewModel()

    val params by viewModel.params.observeAsState()
    val buttonEnabled by viewModel.buttonEnabled.observeAsState(false)
    val progressVisible by viewModel.progressVisibility.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState("")

    AuthLayout<AuthSignUpParamsData>(
        params = params,
        titleH1 = "Sign up with email",
        progressVisible = progressVisible,
        errorMessage = errorMessage,
        buttonName = "Sign up",
        buttonEnabled = buttonEnabled,
        onHomeClicked = { navController.popBackStack() },
        onButtonClicked = viewModel::onSignUpClicked
    )
}

@Composable
@Preview(showBackground = true)
fun AuthSignUpLayoutPreview() {
    LinkitTheme(darkTheme = false) {
        val systemUiController = rememberSystemUiController()
        val navController = rememberNavController()
        systemUiController.setStatusBarColor(White, true)
        AuthSignUpLayout(navController)
    }
}