package com.example.linkit_app.ui.auth.signIn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.linkit_app.ui.auth.AuthLayout
import com.example.linkit_app.ui.theme.LinkitTheme
import com.example.linkit_app.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AuthSignInLayout(navController: NavHostController) {
    val viewModel: AuthSignInViewModel = viewModel()

    val params by viewModel.params.observeAsState()
    val buttonEnabled by viewModel.buttonEnabled.observeAsState(false)
    val progressVisible by viewModel.progressVisibility.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState("")

    AuthLayout<AuthSignInParamsData>(
        params = params,
        titleH1 = "Sign in with email",
        progressVisible = progressVisible,
        errorMessage = errorMessage,
        optionalLayout = {
            Text(
                text = "Forget password?",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .clickable {
                        navController.navigate("reset_password")
                    }
            )
        },
        buttonName = "Sign in",
        buttonEnabled = buttonEnabled,
        onHomeClicked = { navController.popBackStack() },
        onButtonClicked = viewModel::onSignInClicked
    )
}

@Composable
@Preview(showBackground = true)
fun AuthSignInLayoutPreview() {
    LinkitTheme(darkTheme = false) {
        val systemUiController = rememberSystemUiController()
        val navController = rememberNavController()
        systemUiController.setStatusBarColor(White, true)
        AuthSignInLayout(navController)
    }
}