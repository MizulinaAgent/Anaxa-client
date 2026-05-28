package com.example.anaxa.ui.screens.orders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anaxa.domain.model.Order
import com.example.anaxa.domain.repository.OrdersRepository
import com.example.anaxa.ui.util.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrdersUiState(
    val role: String = "buyer",
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository
) : ViewModel() {

    var state by mutableStateOf(OrdersUiState())
        private set

    init {
        load()
    }

    fun setRole(role: String) {
        if (role == state.role) return
        state = state.copy(role = role)
        load()
    }

    fun load() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            ordersRepository.getMyOrders(state.role).fold(
                onSuccess = { state = state.copy(isLoading = false, orders = it) },
                onFailure = { state = state.copy(isLoading = false, error = it.toUserMessage()) }
            )
        }
    }
}
