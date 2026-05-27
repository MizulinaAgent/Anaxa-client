package com.example.anaxa.ui.screens.lots

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anaxa.domain.model.Category
import com.example.anaxa.domain.model.Lot
import com.example.anaxa.domain.repository.GamesRepository
import com.example.anaxa.domain.repository.LotsRepository
import com.example.anaxa.ui.util.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LotsUiState(
    val gameName: String = "",
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Int? = null,
    val lots: List<Lot> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LotsViewModel @Inject constructor(
    private val gamesRepository: GamesRepository,
    private val lotsRepository: LotsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val gameId: Int = savedStateHandle.get<Int>("gameId") ?: 0

    var state by mutableStateOf(LotsUiState())
        private set

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val game = gamesRepository.getGame(gameId).getOrNull()
            gamesRepository.getCategories(gameId).fold(
                onSuccess = { categories ->
                    val firstId = categories.firstOrNull()?.id
                    state = state.copy(
                        gameName = game?.name.orEmpty(),
                        categories = categories,
                        selectedCategoryId = firstId
                    )
                    if (firstId != null) loadLots() else state = state.copy(isLoading = false)
                },
                onFailure = { state = state.copy(isLoading = false, error = it.toUserMessage()) }
            )
        }
    }

    fun selectCategory(categoryId: Int) {
        if (categoryId == state.selectedCategoryId) return
        state = state.copy(selectedCategoryId = categoryId)
        loadLots()
    }

    private fun loadLots() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            lotsRepository.getLots(state.selectedCategoryId, "active").fold(
                onSuccess = { state = state.copy(isLoading = false, lots = it) },
                onFailure = { state = state.copy(isLoading = false, error = it.toUserMessage()) }
            )
        }
    }
}
