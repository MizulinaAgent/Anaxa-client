package com.example.anaxa.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anaxa.domain.model.Review
import com.example.anaxa.domain.model.User
import com.example.anaxa.domain.repository.AuthRepository
import com.example.anaxa.domain.repository.ReviewsRepository
import com.example.anaxa.ui.util.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val reviews: List<Review> = emptyList(),
    val isOwnProfile: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
    val loggedOut: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val reviewsRepository: ReviewsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val targetUserId: String? = savedStateHandle.get<String>("userId")

    var state by mutableStateOf(ProfileUiState(isOwnProfile = targetUserId == null))
        private set

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val userResult = if (targetUserId == null) {
                authRepository.getMe()
            } else {
                authRepository.getUser(targetUserId)
            }
            userResult.fold(
                onSuccess = { user ->
                    state = state.copy(user = user)
                    reviewsRepository.getUserReviews(user.id).fold(
                        onSuccess = { state = state.copy(isLoading = false, reviews = it) },
                        onFailure = { state = state.copy(isLoading = false, error = it.toUserMessage()) }
                    )
                },
                onFailure = { state = state.copy(isLoading = false, error = it.toUserMessage()) }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            state = state.copy(loggedOut = true)
        }
    }
}
