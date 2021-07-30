package com.example.linkit_app.ui.auth.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun AuthSplashLayout(navController: NavHostController) {
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

    viewModel.onSplashFinished { userFound ->
        if (!userFound) {
            navController.navigate("start")
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