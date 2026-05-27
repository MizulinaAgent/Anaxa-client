package com.example.anaxa.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.anaxa.ui.theme.NeonEmerald
import com.example.anaxa.ui.theme.TextMuted
import kotlin.math.floor

@Composable
fun RatingStars(
    rating: Double,
    modifier: Modifier = Modifier,
    starSize: Int = 16,
    showValue: Boolean = true
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        val full = floor(rating).toInt()
        val half = (rating - full) >= 0.5
        for (i in 1..5) {
            val icon = when {
                i <= full -> Icons.Filled.Star
                i == full + 1 && half -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Outlined.StarOutline
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = NeonEmerald,
                modifier = Modifier.size(starSize.dp)
            )
        }
        if (showValue) {
            Text(
                text = " %.1f".format(rating),
                style = MaterialTheme.typography.labelMedium,
                color = TextMuted
            )
        }
    }
}
