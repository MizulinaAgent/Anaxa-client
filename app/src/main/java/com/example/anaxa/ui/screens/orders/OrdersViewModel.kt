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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrdersUiState(
    val role: String = "buyer",
    val buyerOrders: List<Order> = emptyList(),
    val sellerOrders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val orders: List<Order> get() = if (role == "seller") sellerOrders else buyerOrders
    val buyerUnread: Int get() = buyerOrders.sumOf { it.unreadCount }
    val sellerUnread: Int get() = sellerOrders.sumOf { it.unreadCount }
}

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository
) : ViewModel() {

    var state by mutableStateOf(OrdersUiState())
        private set

    init {
        load(showLoading = true)
        startPolling()
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (true) {
                delay(5000)
                if (!state.isLoading) load(showLoading = false)
            }
        }
    }

    fun setRole(role: String) {
        if (role == state.role) return
        state = state.copy(role = role)
    }

    fun load(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) state = state.copy(isLoading = true, error = null)
            val buyer = ordersRepository.getMyOrders("buyer")
            val seller = ordersRepository.getMyOrders("seller")
            val error = buyer.exceptionOrNull() ?: seller.exceptionOrNull()
            state = if (error != null && showLoading) {
                state.copy(isLoading = false, error = error.toUserMessage())
            } else {
                state.copy(
                    isLoading = false,
                    error = null,
                    buyerOrders = buyer.getOrElse { state.buyerOrders },
                    sellerOrders = seller.getOrElse { state.sellerOrders }
                )
            }
        }
    }
}
