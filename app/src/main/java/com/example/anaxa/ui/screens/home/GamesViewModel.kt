package com.example.anaxa.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anaxa.domain.model.Game
import com.example.anaxa.domain.repository.GamesRepository
import com.example.anaxa.domain.repository.OrdersRepository
import com.example.anaxa.ui.util.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GamesUiState(
    val games: List<Game> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val unreadOrders: Int = 0
) {
    val filtered: List<Game>
        get() = if (query.isBlank()) games
        else games.filter { it.name.contains(query, ignoreCase = true) }
}

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val gamesRepository: GamesRepository,
    private val ordersRepository: OrdersRepository
) : ViewModel() {

    var state by mutableStateOf(GamesUiState())
        private set

    init {
        loadGames()
        startUnreadPolling()
    }

    private fun startUnreadPolling() {
        viewModelScope.launch {
            while (true) {
                val buyer = ordersRepository.getMyOrders("buyer").getOrNull().orEmpty()
                val seller = ordersRepository.getMyOrders("seller").getOrNull().orEmpty()
                val total = buyer.sumOf { it.unreadCount } + seller.sumOf { it.unreadCount }
                state = state.copy(unreadOrders = total)
                delay(7000)
            }
        }
    }

    fun loadGames() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            gamesRepository.getGames().fold(
                onSuccess = { state = state.copy(isLoading = false, games = it) },
                onFailure = { state = state.copy(isLoading = false, error = it.toUserMessage()) }
            )
        }
    }

    fun onSearchChange(value: String) {
        state = state.copy(query = value)
    }
}
