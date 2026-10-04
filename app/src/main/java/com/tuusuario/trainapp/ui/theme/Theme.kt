package com.tuusuario.trainapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFD600),      // Yellow accent
    onPrimary = Color(0xFF121212),    // Black on yellow
    primaryContainer = Color(0xFF3D3D00),
    onPrimaryContainer = Color(0xFFFFD600),
    secondary = Color(0xFFBDBDBD),    // Gray
    onSecondary = Color(0xFF121212),
    tertiary = Color(0xFF4CAF50),     // Green for success
    onTertiary = Color(0xFF121212),
    error = Color(0xFFEF5350),
    onError = Color(0xFF121212),
    background = Color(0xFF121212),   // Near black
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1E1E1E),      // Dark gray
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFBDBDBD),
    outline = Color(0xFF4A4A4A),
    inverseSurface = Color(0xFFE0E0E0),
    inverseOnSurface = Color(0xFF121212),
    surfaceTint = Color(0xFFFFD600),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFF9A825),
    onPrimary = Color(0xFF121212),
    primaryContainer = Color(0xFFFFD600),
    onPrimaryContainer = Color(0xFF121212),
    secondary = Color(0xFF757575),
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF388E3C),
    onTertiary = Color(0xFFFFFFFF),
    error = Color(0xFFD32F2F),
    onError = Color(0xFFFFFFFF),
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF121212),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF121212),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF757575),
    outline = Color(0xFFBDBDBD),
    inverseSurface = Color(0xFF121212),
    inverseOnSurface = Color(0xFFFAFAFA),
    surfaceTint = Color(0xFFF9A825),
)

@Composable
fun Theme(
    darkTheme: Boolean = true, // Force dark theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}