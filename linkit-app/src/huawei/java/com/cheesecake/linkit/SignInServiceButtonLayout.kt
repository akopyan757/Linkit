package com.cheesecake.linkit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cheesecake.linkit.ui.theme.Black
import com.cheesecake.linkit.ui.theme.LinkitTheme

@Composable
fun SignInServiceButtonLayout(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFCE0E2D)
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_login_icon_huawei),
            contentDescription = "Icon"
        )

        Text(
            text = "Sign in with Google",
            color = Black,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GoogleSignInButtonLayoutPreview() {
    LinkitTheme {
        SignInServiceButtonLayout()
    }
}