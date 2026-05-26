package com.example.anaxa.ui.theme

import androidx.compose.ui.graphics.Brush

val EmeraldGradient = Brush.horizontalGradient(
    colors = listOf(EmeraldMid, NeonEmerald, NeonBright)
)

val EmeraldGradientVertical = Brush.verticalGradient(
    colors = listOf(NeonBright, NeonEmerald, EmeraldMid)
)

val BackgroundGradient = Brush.verticalGradient(
    colors = listOf(Background, Surface)
)

fun glowGradient(center: androidx.compose.ui.graphics.Color = NeonEmerald) = Brush.radialGradient(
    colors = listOf(center.copy(alpha = 0.35f), center.copy(alpha = 0f))
)
