package com.mansur.walawili.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define colors - Updated with new blues
private val PrimaryColor = Color(0xFF1F6FE6)          // Bright Blue
private val PrimaryContainerColor = Color(0xFF2E2C8C) // Deep Blue
private val SecondaryColor = Color(0xFF1B1A38)        // Dark Navy
private val TertiaryColor = Color(0xFF999999)
private val BackgroundColor = Color.White
private val SurfaceColor = Color(0xFFF5F5F5)
private val ErrorColor = Color(0xFFB3261E)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    primaryContainer = PrimaryContainerColor,
    secondary = SecondaryColor,
    tertiary = TertiaryColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    error = ErrorColor,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A),
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6BA3FF),
    primaryContainer = Color(0xFF1F6FE6),
    secondary = Color(0xFF8B89D4),
    tertiary = Color(0xFFBBBBBB),
    background = Color(0xFF1A1A1A),
    surface = Color(0xFF2A2A2A),
    error = Color(0xFFF2B8B5),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color(0xFF2A2A2A),
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color(0xFF601410)
)

@Composable
fun WalaWiliTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}