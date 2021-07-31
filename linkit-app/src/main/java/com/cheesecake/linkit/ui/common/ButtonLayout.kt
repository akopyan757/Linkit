package com.cheesecake.linkit.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cheesecake.linkit.ui.theme.Black
import com.cheesecake.linkit.ui.theme.Grey
import com.cheesecake.linkit.ui.theme.LinkitTheme

@Composable
fun ButtonLayout(
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().height(52.dp)
    ) {
        Text(
            text = text,
            color = if (enabled) Black else Grey,
            style = MaterialTheme.typography.body1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonLayoutPreview() {
    LinkitTheme {
        ButtonLayout(
            text = "Button",
            enabled = true,
            Modifier.padding(24.dp)
        ) {}
    }
}


@Preview(showBackground = true)
@Composable
fun ButtonLayoutDisabledPreview() {
    LinkitTheme {
        ButtonLayout(
            text = "Button Disabled",
            enabled = false,
            Modifier.padding(24.dp)
        ) {}
    }
}