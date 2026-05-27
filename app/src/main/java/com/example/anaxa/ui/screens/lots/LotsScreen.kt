package com.example.anaxa.ui.screens.lots

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.anaxa.ui.components.EmptyView
import com.example.anaxa.ui.components.ErrorView
import com.example.anaxa.ui.components.LoadingIndicator
import com.example.anaxa.ui.components.LotCard
import com.example.anaxa.ui.theme.Background
import com.example.anaxa.ui.theme.NeonEmerald
import com.example.anaxa.ui.theme.SurfaceVariant
import com.example.anaxa.ui.theme.TextSecondary
import com.example.anaxa.ui.util.categoryLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotsScreen(
    onBack: () -> Unit,
    onLotClick: (String) -> Unit,
    viewModel: LotsViewModel = hiltViewModel()
) {
    val state = viewModel.state

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.gameName.ifBlank { "Лоты" },
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = NeonEmerald)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.categories) { category ->
                    FilterChip(
                        selected = category.id == state.selectedCategoryId,
                        onClick = { viewModel.selectCategory(category.id) },
                        label = { Text(categoryLabel(category.type)) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonEmerald,
                            selectedLabelColor = Background,
                            containerColor = SurfaceVariant,
                            labelColor = TextSecondary
                        )
                    )
                }
            }

            when {
                state.isLoading -> LoadingIndicator()
                state.error != null -> ErrorView(state.error, onRetry = viewModel::load)
                state.lots.isEmpty() -> EmptyView("В этой категории пока нет лотов")
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.lots) { lot ->
                        LotCard(lot = lot, onClick = { onLotClick(lot.id) })
                    }
                }
            }
        }
    }
}
