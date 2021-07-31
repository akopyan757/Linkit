package com.cheesecake.linkit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    secondary = Secondary,
    secondaryVariant = Disabled,
    surface = Surface,
    error = Error,
    background = Background,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onSurface = Color.Black,
    onBackground = Color.White,
    onError = Color.White
)

@Composable
fun LinkitTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}