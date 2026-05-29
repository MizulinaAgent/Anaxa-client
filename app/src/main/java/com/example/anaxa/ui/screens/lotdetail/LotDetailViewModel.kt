package com.example.anaxa.ui.screens.lotdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anaxa.domain.model.Lot
import com.example.anaxa.domain.repository.LotsRepository
import com.example.anaxa.domain.repository.OrdersRepository
import com.example.anaxa.ui.util.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LotDetailUiState(
    val lot: Lot? = null,
    val quantity: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isBuying: Boolean = false,
    val buyError: String? = null,
    val createdOrderId: String? = null
)

@HiltViewModel
class LotDetailViewModel @Inject constructor(
    private val lotsRepository: LotsRepository,
    private val ordersRepository: OrdersRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val lotId: String = savedStateHandle.get<String>("lotId").orEmpty()

    var state by mutableStateOf(LotDetailUiState())
        private set

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            lotsRepository.getLot(lotId).fold(
                onSuccess = { state = state.copy(isLoading = false, lot = it) },
                onFailure = { state = state.copy(isLoading = false, error = it.toUserMessage()) }
            )
        }
    }

    fun setQuantity(value: Int) {
        val max = state.lot?.quantity ?: 1
        state = state.copy(quantity = value.coerceIn(1, maxOf(1, max)))
    }

    fun buy() {
        if (state.isBuying) return
        viewModelScope.launch {
            state = state.copy(isBuying = true, buyError = null)
            ordersRepository.createOrder(lotId, state.quantity).fold(
                onSuccess = { state = state.copy(isBuying = false, createdOrderId = it.id) },
                onFailure = { state = state.copy(isBuying = false, buyError = it.toUserMessage()) }
            )
        }
    }
}
