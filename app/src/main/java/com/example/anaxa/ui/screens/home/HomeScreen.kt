package com.example.anaxa.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.anaxa.ui.components.AnaxaTextField
import com.example.anaxa.ui.components.EmptyView
import com.example.anaxa.ui.components.ErrorView
import com.example.anaxa.ui.components.GameCard
import com.example.anaxa.ui.components.LoadingIndicator
import com.example.anaxa.ui.theme.Background
import com.example.anaxa.ui.theme.NeonEmerald
import com.example.anaxa.ui.theme.NeonMint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGameClick: (Int) -> Unit,
    onMyLotsClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCreateLotClick: () -> Unit,
    viewModel: GamesViewModel = hiltViewModel()
) {
    val state = viewModel.state

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ANAXA",
                        color = NeonMint,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onMyLotsClick) {
                        Icon(Icons.Filled.Storefront, contentDescription = "Мои лоты", tint = NeonEmerald)
                    }
                    IconButton(onClick = onOrdersClick) {
                        BadgedBox(
                            badge = {
                                if (state.unreadOrders > 0) {
                                    Badge(containerColor = NeonEmerald, contentColor = Background) {
                                        Text(if (state.unreadOrders > 9) "9+" else state.unreadOrders.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Filled.ShoppingBag, contentDescription = "Заказы", tint = NeonEmerald)
                        }
                    }
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Filled.Person, contentDescription = "Профиль", tint = NeonEmerald)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateLotClick,
                containerColor = NeonEmerald,
                contentColor = Background
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Создать лот")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            AnaxaTextField(
                value = state.query,
                onValueChange = viewModel::onSearchChange,
                label = "Поиск игр",
                leadingIcon = Icons.Filled.Search,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            when {
                state.isLoading -> LoadingIndicator()
                state.error != null -> ErrorView(state.error, onRetry = viewModel::loadGames)
                state.filtered.isEmpty() -> EmptyView("Игры не найдены")
                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.filtered) { game ->
                        GameCard(game = game, onClick = { onGameClick(game.id) })
                    }
                }
            }
        }
    }
}
