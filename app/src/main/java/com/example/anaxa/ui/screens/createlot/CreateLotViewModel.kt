package com.example.anaxa.ui.screens.createlot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anaxa.domain.model.Category
import com.example.anaxa.domain.model.Game
import com.example.anaxa.domain.repository.GamesRepository
import com.example.anaxa.domain.repository.LotsRepository
import com.example.anaxa.ui.util.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateLotUiState(
    val games: List<Game> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedGameId: Int? = null,
    val selectedCategoryId: Int? = null,
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val quantity: String = "1",
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val created: Boolean = false
) {
    val selectedGameName: String
        get() = games.firstOrNull { it.id == selectedGameId }?.name.orEmpty()

    val canSubmit: Boolean
        get() = selectedCategoryId != null && title.isNotBlank() &&
            (price.toDoubleOrNull() ?: 0.0) > 0.0 &&
            (quantity.toIntOrNull() ?: 0) >= 1
}

@HiltViewModel
class CreateLotViewModel @Inject constructor(
    private val gamesRepository: GamesRepository,
    private val lotsRepository: LotsRepository
) : ViewModel() {

    var state by mutableStateOf(CreateLotUiState())
        private set

    init {
        loadGames()
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

    fun selectGame(gameId: Int) {
        state = state.copy(selectedGameId = gameId, selectedCategoryId = null, categories = emptyList())
        viewModelScope.launch {
            gamesRepository.getCategories(gameId).fold(
                onSuccess = { state = state.copy(categories = it) },
                onFailure = { state = state.copy(error = it.toUserMessage()) }
            )
        }
    }

    fun selectCategory(categoryId: Int) { state = state.copy(selectedCategoryId = categoryId) }
    fun onTitleChange(value: String) { state = state.copy(title = value, error = null) }
    fun onDescriptionChange(value: String) { state = state.copy(description = value) }
    fun onPriceChange(value: String) { state = state.copy(price = value.filter { it.isDigit() || it == '.' }) }
    fun onQuantityChange(value: String) { state = state.copy(quantity = value.filter { it.isDigit() }) }

    fun submit() {
        val current = state
        if (current.isSubmitting || !current.canSubmit) return
        viewModelScope.launch {
            state = state.copy(isSubmitting = true, error = null)
            lotsRepository.createLot(
                categoryId = current.selectedCategoryId!!,
                title = current.title.trim(),
                description = current.description.trim().ifBlank { null },
                price = current.price.toDouble(),
                quantity = current.quantity.toInt()
            ).fold(
                onSuccess = { state = state.copy(isSubmitting = false, created = true) },
                onFailure = { state = state.copy(isSubmitting = false, error = it.toUserMessage()) }
            )
        }
    }
}
