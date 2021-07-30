package com.example.linkit_app.ui.auth.start

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.linkit_app.ui.common.ButtonLayout
import com.example.linkit_app.ui.theme.LinkitTheme
import com.example.linkit_app.ui.theme.PoppinsFamily
import com.example.linkit_app.ui.theme.White
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AuthStartLayout(navController: NavHostController) {

    val viewModel: AuthStartViewModel = viewModel()
    val progressVisible by viewModel.progressVisibility.observeAsState(false)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Linkit",
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Bottom)
                .weight(2f)
        )

        ButtonLayout(
            text = "Sign up with email",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Bottom)
                .padding(top = 20.dp),
            onClick = { navController.navigate("sign_up") }
        )

        ButtonLayout(
            text = "Sign up with service",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Bottom)
                .padding(top = 10.dp)
        )

        ClickableText(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Medium,
                        fontFamily = PoppinsFamily
                    )
                ) { append("Already have an account?") }
            },
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(top = 20.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .wrapContentHeight(Alignment.CenterVertically),
            onClick = { navController.navigate("sign_in") }
        )

        if (progressVisible) {
            CircularProgressIndicator(
                strokeWidth = 5.dp,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .wrapContentWidth()
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Text(
            text = "By signing up, you agree to UnboxMe’s",
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "Terms of Use and Privacy Policy.",
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
@Preview(showBackground = true)
fun AuthStartLayoutPreview() {
    LinkitTheme(darkTheme = false) {
        val systemUiController = rememberSystemUiController()
        val navController = rememberNavController()
        systemUiController.setStatusBarColor(White, true)
        AuthStartLayout(navController)
    }
}