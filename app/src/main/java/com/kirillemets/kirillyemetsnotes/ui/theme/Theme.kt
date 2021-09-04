package com.kirillemets.kirillyemetsnotes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xff9b59b6),
    primaryVariant = Color(0xff000000),
    secondary = Color(0xff9b59b6),
    secondaryVariant = Color(0xff9b59b6),
    background = Color(0xff000000),
)

private val LightColorPalette = lightColors(
    primary = Color(0xff9b59b6),
    primaryVariant = Color(0xff6b2c86),
    secondary = Color(0xff9b59b6),
    background = Color(0xFFEEEEEE),

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun KirillYemetsNotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}