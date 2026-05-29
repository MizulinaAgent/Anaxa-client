package com.example.anaxa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.anaxa.domain.model.Game
import com.example.anaxa.domain.model.Lot
import com.example.anaxa.ui.theme.EmeraldGradient
import com.example.anaxa.ui.theme.NeonEmerald
import com.example.anaxa.ui.theme.Surface
import com.example.anaxa.ui.theme.TextMuted

@Composable
fun GameCard(game: Game, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.small)
                    .background(EmeraldGradient),
                contentAlignment = Alignment.Center
            ) {
                if (!game.iconUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = game.iconUrl,
                        contentDescription = game.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                    )
                } else {
                    Text(
                        text = game.name.take(1).uppercase(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = com.example.anaxa.ui.theme.Background,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = game.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun LotCard(lot: Lot, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = lot.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (!lot.description.isNullOrBlank()) {
                Text(
                    text = lot.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Text(
                text = "В наличии: ${lot.quantity} шт.",
                style = MaterialTheme.typography.labelMedium,
                color = TextMuted,
                modifier = Modifier.padding(top = 6.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(EmeraldGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lot.seller.username.take(1).uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = com.example.anaxa.ui.theme.Background
                        )
                    }
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = lot.seller.username,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        RatingStars(rating = lot.seller.rating, starSize = 12)
                    }
                }
                Text(
                    text = "%.0f ₽".format(lot.price),
                    style = MaterialTheme.typography.titleMedium,
                    color = NeonEmerald,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
