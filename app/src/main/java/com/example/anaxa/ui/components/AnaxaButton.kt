package com.example.anaxa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import com.example.anaxa.ui.theme.Background
import com.example.anaxa.ui.theme.EmeraldGradient
import com.example.anaxa.ui.theme.SurfaceVariant
import com.example.anaxa.ui.theme.TextMuted

@Composable
fun AnaxaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false
) {
    val active = enabled && !loading
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(if (enabled) EmeraldGradient else SolidColor(SurfaceVariant))
            .clickable(enabled = active) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = Background,
                strokeWidth = 2.dp,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Text(
                text = text,
                color = if (enabled) Background else TextMuted,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
