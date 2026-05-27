package com.example.anaxa.ui.screens.lotdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.anaxa.ui.components.AnaxaButton
import com.example.anaxa.ui.components.ErrorView
import com.example.anaxa.ui.components.LoadingIndicator
import com.example.anaxa.ui.components.RatingStars
import com.example.anaxa.ui.theme.Background
import com.example.anaxa.ui.theme.EmeraldGradient
import com.example.anaxa.ui.theme.ErrorRed
import com.example.anaxa.ui.theme.NeonEmerald
import com.example.anaxa.ui.theme.Surface
import com.example.anaxa.ui.theme.TextMuted
import com.example.anaxa.ui.theme.TextSecondary
import com.example.anaxa.ui.util.categoryLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotDetailScreen(
    onBack: () -> Unit,
    onOrderCreated: (String) -> Unit,
    onSellerClick: (String) -> Unit,
    viewModel: LotDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(state.createdOrderId) {
        state.createdOrderId?.let(onOrderCreated)
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Лот", color = TextSecondary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = NeonEmerald)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        bottomBar = {
            val lot = state.lot
            if (lot != null) {
                Column(modifier = Modifier.background(Background).padding(16.dp)) {
                    if (state.buyError != null) {
                        Text(
                            text = state.buyError,
                            color = ErrorRed,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    AnaxaButton(
                        text = "Купить за %.0f ₽".format(lot.price),
                        onClick = viewModel::buy,
                        loading = state.isBuying
                    )
                }
            }
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator(modifier = Modifier.padding(padding))
            state.error != null -> ErrorView(state.error, modifier = Modifier.padding(padding), onRetry = viewModel::load)
            state.lot != null -> {
                val lot = state.lot!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = lot.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = "%.0f ₽".format(lot.price),
                        style = MaterialTheme.typography.headlineLarge,
                        color = NeonEmerald,
                        fontWeight = FontWeight.Bold
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onSellerClick(lot.seller.id) },
                        colors = CardDefaults.cardColors(containerColor = Surface)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(44.dp).clip(CircleShape).background(EmeraldGradient),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = lot.seller.username.take(1).uppercase(),
                                    color = Background,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(modifier = Modifier.padding(start = 12.dp)) {
                                Text(
                                    text = lot.seller.username,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextSecondary
                                )
                                RatingStars(rating = lot.seller.rating, starSize = 14)
                            }
                        }
                    }

                    if (!lot.description.isNullOrBlank()) {
                        Text(
                            text = "Описание",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = lot.description!!,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}
