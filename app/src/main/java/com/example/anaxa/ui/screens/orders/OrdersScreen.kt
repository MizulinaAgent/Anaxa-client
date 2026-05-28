package com.example.anaxa.ui.screens.orders

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.anaxa.domain.model.Order
import com.example.anaxa.ui.components.EmptyView
import com.example.anaxa.ui.components.ErrorView
import com.example.anaxa.ui.components.LoadingIndicator
import com.example.anaxa.ui.theme.Background
import com.example.anaxa.ui.theme.NeonEmerald
import com.example.anaxa.ui.theme.Surface
import com.example.anaxa.ui.theme.SurfaceVariant
import com.example.anaxa.ui.theme.TextMuted
import com.example.anaxa.ui.theme.TextSecondary
import com.example.anaxa.ui.util.orderStatusLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    onBack: () -> Unit,
    onOrderClick: (String) -> Unit,
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val state = viewModel.state

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text("Заказы", color = TextSecondary, fontWeight = FontWeight.Bold) },
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
            TabRow(
                selectedTabIndex = if (state.role == "seller") 1 else 0,
                containerColor = Background,
                contentColor = NeonEmerald
            ) {
                Tab(
                    selected = state.role == "buyer",
                    onClick = { viewModel.setRole("buyer") },
                    text = { Text("Покупки") }
                )
                Tab(
                    selected = state.role == "seller",
                    onClick = { viewModel.setRole("seller") },
                    text = { Text("Продажи") }
                )
            }

            when {
                state.isLoading -> LoadingIndicator()
                state.error != null -> ErrorView(state.error, onRetry = viewModel::load)
                state.orders.isEmpty() -> EmptyView(
                    if (state.role == "seller") "У вас нет продаж" else "У вас нет покупок"
                )
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.orders) { order ->
                        OrderCard(
                            order = order,
                            myRole = state.role,
                            onClick = { onOrderClick(order.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order, myRole: String, onClick: () -> Unit) {
    val counterparty = if (myRole == "seller") order.buyer else order.seller
    val counterpartyLabel = if (myRole == "seller") "Покупатель" else "Продавец"

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = order.lot.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "%.0f ₽".format(order.lot.price),
                    style = MaterialTheme.typography.titleMedium,
                    color = NeonEmerald,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(counterpartyLabel, style = MaterialTheme.typography.labelMedium, color = TextMuted)
                    Text(
                        counterparty.username,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
                Text(
                    text = orderStatusLabel(order.status),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(SurfaceVariant)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}
