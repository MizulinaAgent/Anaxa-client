package com.example.anaxa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AnaxaColorScheme = darkColorScheme(
    primary = NeonEmerald,
    onPrimary = Background,
    primaryContainer = EmeraldDeep,
    onPrimaryContainer = NeonMint,
    secondary = AquaCyan,
    onSecondary = Background,
    secondaryContainer = EmeraldMid,
    onSecondaryContainer = TextPrimary,
    tertiary = EmeraldLight,
    onTertiary = Background,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextSecondary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextMuted,
    outline = TextMuted,
    error = ErrorRed,
    onError = TextPrimary
)

@Composable
fun AnaxaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AnaxaColorScheme,
        typography = Typography,
        content = content
    )
}
