package com.example.anaxa.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anaxa.domain.model.Game
import com.example.anaxa.domain.repository.GamesRepository
import com.example.anaxa.ui.util.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GamesUiState(
    val games: List<Game> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val filtered: List<Game>
        get() = if (query.isBlank()) games
        else games.filter { it.name.contains(query, ignoreCase = true) }
}

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val gamesRepository: GamesRepository
) : ViewModel() {

    var state by mutableStateOf(GamesUiState())
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

    fun onSearchChange(value: String) {
        state = state.copy(query = value)
    }
}
