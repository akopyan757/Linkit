package com.example.linkit_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.linkit_app.ui.authSignIn.AuthSignInLayout
import com.example.linkit_app.ui.theme.LinkitTheme
import com.example.linkit_app.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(White, true)
            LinkitTheme {
                AuthSignInLayout()
            }
        }
    }
}