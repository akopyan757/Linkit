package com.example.linkit_app.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.linkit_app.R
import com.example.linkit_app.ui.common.BaseParamsData
import com.example.linkit_app.ui.common.ButtonLayout
import com.example.linkit_app.ui.theme.Error

@Composable
fun <ParamsData : BaseParamsData> AuthLayout(
    params: ParamsData?,
    titleH1: String,
    progressVisible: Boolean,
    errorMessage: String,
    optionalLayout: @Composable () -> Unit = {},
    buttonName: String,
    buttonEnabled: Boolean,
    onHomeClicked: () -> Unit = {},
    onButtonClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "Home",
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .align(Alignment.Start)
                .clickable { onHomeClicked.invoke() },
        )

        Text(
            text = titleH1,
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Bottom)
                .weight(1f)
        )

        params?.getInputItems()?.forEach { inputItem ->
            Spacer(modifier = Modifier.height(16.dp))
            inputItem.getInputLayout()
        }

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.overline,
            color = Error,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp, start = 16.dp)
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

        optionalLayout.invoke()

        ButtonLayout(
            text = buttonName,
            enabled = buttonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.Bottom),
            onClick = onButtonClicked
        )
    }
}