package com.example.anaxa.ui.screens.mylots

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anaxa.domain.model.Lot
import com.example.anaxa.domain.repository.AuthRepository
import com.example.anaxa.domain.repository.LotsRepository
import com.example.anaxa.ui.util.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyLotsUiState(
    val lots: List<Lot> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

@HiltViewModel
class MyLotsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val lotsRepository: LotsRepository
) : ViewModel() {

    var state by mutableStateOf(MyLotsUiState())
        private set

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val me = authRepository.getMe().getOrNull()
            if (me == null) {
                state = state.copy(isLoading = false, error = "Не удалось определить пользователя")
                return@launch
            }
            lotsRepository.getLots(categoryId = null, status = null, sellerId = me.id).fold(
                onSuccess = { state = state.copy(isLoading = false, lots = it) },
                onFailure = { state = state.copy(isLoading = false, error = it.toUserMessage()) }
            )
        }
    }

    fun delete(lotId: String) {
        viewModelScope.launch {
            lotsRepository.deleteLot(lotId).fold(
                onSuccess = {
                    state = state.copy(
                        lots = state.lots.filterNot { it.id == lotId },
                        message = "Лот удалён"
                    )
                },
                onFailure = { state = state.copy(message = it.toUserMessage()) }
            )
        }
    }

    fun consumeMessage() {
        state = state.copy(message = null)
    }
}
