package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val KaliDarkColorScheme = darkColorScheme(
    primary = KaliGreen,
    onPrimary = KaliDarkBg,
    primaryContainer = KaliSurfaceVariant,
    onPrimaryContainer = KaliGreen,
    secondary = KaliCyan,
    onSecondary = KaliDarkBg,
    secondaryContainer = KaliSurfaceVariant,
    onSecondaryContainer = KaliCyan,
    tertiary = KaliYellow,
    background = KaliDarkBg,
    onBackground = KaliTextPrimary,
    surface = KaliSurface,
    onSurface = KaliTextPrimary,
    surfaceVariant = KaliSurfaceVariant,
    onSurfaceVariant = KaliTextSecondary,
    error = KaliRed,
    onError = KaliDarkBg
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force default cyberpunk dark theme for hacker aesthetic
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = KaliDarkColorScheme,
        typography = Typography,
        content = content
    )
}
