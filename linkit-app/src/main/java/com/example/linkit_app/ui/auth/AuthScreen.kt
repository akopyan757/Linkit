package com.example.linkit_app.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.linkit_app.ui.auth.resetPassword.AuthResetPasswordLayout
import com.example.linkit_app.ui.auth.signIn.AuthSignInLayout
import com.example.linkit_app.ui.auth.signUp.AuthSignUpLayout
import com.example.linkit_app.ui.auth.splash.AuthSplashLayout
import com.example.linkit_app.ui.auth.start.AuthStartLayout
import com.example.linkit_app.ui.theme.LinkitTheme
import com.example.linkit_app.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AuthScreen() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "splash") {
        composable("splash") { AuthSplashLayout(navController = navController) }
        composable("start") { AuthStartLayout(navController = navController) }
        composable("sign_up") { AuthSignUpLayout(navController = navController) }
        composable("sign_in") { AuthSignInLayout(navController = navController) }
        composable("reset_password") { AuthResetPasswordLayout(navController = navController) }
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