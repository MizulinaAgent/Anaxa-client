package com.example.anaxa.ui.screens.mylots

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.anaxa.domain.model.Lot
import com.example.anaxa.ui.components.EmptyView
import com.example.anaxa.ui.components.ErrorView
import com.example.anaxa.ui.components.LoadingIndicator
import com.example.anaxa.ui.theme.Background
import com.example.anaxa.ui.theme.ErrorRed
import com.example.anaxa.ui.theme.NeonEmerald
import com.example.anaxa.ui.theme.Surface
import com.example.anaxa.ui.theme.TextMuted
import com.example.anaxa.ui.theme.TextSecondary
import com.example.anaxa.ui.util.lotStatusLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLotsScreen(
    onBack: () -> Unit,
    onLotClick: (String) -> Unit,
    viewModel: MyLotsViewModel = hiltViewModel()
) {
    val state = viewModel.state

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Мои лоты", color = TextSecondary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = NeonEmerald)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator(modifier = Modifier.padding(padding))
            state.error != null -> ErrorView(state.error, modifier = Modifier.padding(padding), onRetry = viewModel::load)
            state.lots.isEmpty() -> EmptyView("У вас пока нет лотов", modifier = Modifier.padding(padding))
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.lots) { lot ->
                    MyLotCard(
                        lot = lot,
                        onClick = { onLotClick(lot.id) },
                        onDelete = { viewModel.delete(lot.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MyLotCard(lot: Lot, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lot.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${lotStatusLabel(lot.status)} · %.0f ₽".format(lot.price),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.DeleteOutline, contentDescription = "Удалить", tint = ErrorRed)
            }
        }
    }
}
