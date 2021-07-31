package com.cheesecake.linkit

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cheesecake.linkit.ui.auth.AuthScreen
import com.cheesecake.linkit.ui.theme.LinkitTheme
import com.cheesecake.linkit.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(White, true)
            LinkitTheme {
                AuthScreen { userUid ->
                    startActivity(Intent(this, LinkitActivity::class.java))
                }
            }
        }
    }
}