package com.cheesecake.linkit.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cheesecake.linkit.ui.auth.resetPassword.AuthResetPasswordLayout
import com.cheesecake.linkit.ui.auth.signIn.AuthSignInLayout
import com.cheesecake.linkit.ui.auth.signUp.AuthSignUpLayout
import com.cheesecake.linkit.ui.auth.splash.AuthSplashLayout
import com.cheesecake.linkit.ui.auth.start.AuthStartLayout
import com.cheesecake.linkit.ui.theme.LinkitTheme
import com.cheesecake.linkit.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AuthScreen(onMainStart: (uid: String) -> Unit = {}) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "splash") {
        composable("splash") { AuthSplashLayout(navController, onMainStart) }
        composable("start") { AuthStartLayout(navController) }
        composable("sign_up") { AuthSignUpLayout(navController, onMainStart) }
        composable("sign_in") { AuthSignInLayout(navController, onMainStart) }
        composable("reset_password") { AuthResetPasswordLayout(navController) }
    }
}


@Composable
@Preview(showBackground = true)
fun AuthScreenPreview() {
    LinkitTheme(darkTheme = false) {
        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(White, true)
        AuthScreen()
    }
}