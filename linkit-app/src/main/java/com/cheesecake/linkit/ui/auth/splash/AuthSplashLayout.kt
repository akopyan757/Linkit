package com.cheesecake.linkit.ui.auth.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cheesecake.linkit.ui.theme.LinkitTheme
import com.cheesecake.linkit.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AuthSplashLayout(navController: NavController, onMainStart: (uid: String) -> Unit = {}) {
    val viewModel: AuthSplashViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(
            text = "Splash",
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
                .padding(24.dp)
        )
    }

    viewModel.requestUser { state ->
        when (state) {
            AuthSplashViewModel.State.Loading -> {}

            AuthSplashViewModel.State.UserNotFound -> {
                navController.navigate("start")
            }
            is AuthSplashViewModel.State.UserAuthorized -> {
                onMainStart.invoke(state.uid)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AuthSplashLayoutPreview() {
    LinkitTheme(darkTheme = false) {
        val systemUiController = rememberSystemUiController()
        val navController = rememberNavController()
        systemUiController.setStatusBarColor(White, true)
        AuthSplashLayout(navController)
    }
}