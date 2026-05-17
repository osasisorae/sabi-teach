package com.sabiteach.nativeapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF24543F),
    onPrimary = Color.White,
    secondary = Color(0xFFE7F3EA),
    onSecondary = Color(0xFF24543F),
    background = Color(0xFFF8F3EA),
    surface = Color.White,
    onBackground = Color(0xFF17303A),
    onSurface = Color(0xFF17303A)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7CD0A4),
    onPrimary = Color(0xFF103523),
    background = Color(0xFF112127),
    surface = Color(0xFF17303A),
    onBackground = Color(0xFFF4EFE6),
    onSurface = Color(0xFFF4EFE6)
)

@Composable
fun SabiTeachTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
