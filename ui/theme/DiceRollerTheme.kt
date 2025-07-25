package com.tiation.dnddiceroller.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Player Theme Colors
private val PlayerLightColors = lightColorScheme(
    primary = Color(0xFF4B6A88),          // Muted blue for primary actions
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF78A1BB),        // Lighter blue for secondary elements
    onSecondary = Color(0xFF000000),
    tertiary = Color(0xFFB0C4DE),         // Light steel blue for tertiary elements
    background = Color(0xFFF5F5F5),       // Light grey background
    surface = Color(0xFFFFFFFF),          // White surface
    error = Color(0xFFB00020),            // Standard error red
)

private val PlayerDarkColors = darkColorScheme(
    primary = Color(0xFF78A1BB),          // Lighter blue for dark mode
    onPrimary = Color(0xFF000000),
    secondary = Color(0xFF4B6A88),        // Darker blue for secondary
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF6B8AA8),         // Mid-tone blue for tertiary
    background = Color(0xFF121212),       // Dark background
    surface = Color(0xFF1E1E1E),          // Slightly lighter surface
    error = Color(0xFFCF6679),            // Dark mode error color
)

// DM Theme Colors - More dramatic and darker
private val DMLightColors = lightColorScheme(
    primary = Color(0xFF8B0000),          // Dark red for primary
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF4A0404),        // Deeper red for secondary
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF800020),         // Burgundy for tertiary
    background = Color(0xFF2B1B17),       // Dark brown background
    surface = Color(0xFF3D2B1F),          // Lighter brown surface
    error = Color(0xFFFF4444),            // Bright red for errors
)

private val DMDarkColors = darkColorScheme(
    primary = Color(0xFFFF2400),          // Bright red for dark mode
    onPrimary = Color(0xFF000000),
    secondary = Color(0xFFB22222),        // Fire brick red
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF8B0000),         // Dark red
    background = Color(0xFF1A0F0F),       // Very dark red-tinted background
    surface = Color(0xFF2D1F1F),          // Dark red-tinted surface
    error = Color(0xFFFF6B6B),            // Lighter red for errors
)

// Custom theme properties
data class DiceRollerCustomTheme(
    val isDMMode: Boolean,
    val isAccessibilityMode: Boolean,
    // Add custom theme attributes here
    val diceHighlightColor: Color,
    val resultTextColor: Color,
    val accentBorderColor: Color
)

// Local composition for custom theme
val LocalDiceRollerCustomTheme = staticCompositionLocalOf {
    DiceRollerCustomTheme(
        isDMMode = false,
        isAccessibilityMode = false,
        diceHighlightColor = Color.Unspecified,
        resultTextColor = Color.Unspecified,
        accentBorderColor = Color.Unspecified
    )
}

@Composable
fun DiceRollerTheme(
    isDMMode: Boolean = false,
    isAccessibilityMode: Boolean = false,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isDMMode && darkTheme -> DMDarkColors
        isDMMode && !darkTheme -> DMLightColors
        darkTheme -> PlayerDarkColors
        else -> PlayerLightColors
    }

    val customTheme = DiceRollerCustomTheme(
        isDMMode = isDMMode,
        isAccessibilityMode = isAccessibilityMode,
        diceHighlightColor = if (isDMMode) Color(0xFFFF4444) else Color(0xFF4B6A88),
        resultTextColor = if (isDMMode) Color(0xFFFFD700) else Color(0xFF000000),
        accentBorderColor = if (isDMMode) Color(0xFF8B0000) else Color(0xFF78A1BB)
    )

    CompositionLocalProvider(
        LocalDiceRollerCustomTheme provides customTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
