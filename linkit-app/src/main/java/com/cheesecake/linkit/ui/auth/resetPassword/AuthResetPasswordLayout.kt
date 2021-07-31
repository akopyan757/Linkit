package com.cheesecake.linkit.ui.auth.resetPassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cheesecake.linkit.ui.auth.AuthLayout
import com.cheesecake.linkit.ui.theme.LinkitTheme
import com.cheesecake.linkit.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AuthResetPasswordLayout(navController: NavHostController) {
    val viewModel: AuthResetPasswordViewModel = viewModel()

    val params by viewModel.params.observeAsState()
    val buttonEnabled by viewModel.buttonEnabled.observeAsState(false)
    val progressVisible by viewModel.progressVisibility.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState("")

    AuthLayout<AuthResetPasswordParamsData>(
        params = params,
        titleH1 = "Password reset",
        progressVisible = progressVisible,
        errorMessage = errorMessage,
        buttonName = "Send",
        buttonEnabled = buttonEnabled,
        onHomeClicked = { navController.popBackStack() },
        onButtonClicked = viewModel::onResetPasswordClicked
    )
}

@Composable
@Preview(showBackground = true)
fun AuthSignInLayoutPreview() {
    LinkitTheme(darkTheme = false) {
        val systemUiController = rememberSystemUiController()
        val navController = rememberNavController()
        systemUiController.setStatusBarColor(White, true)
        AuthResetPasswordLayout(navController)
    }
}