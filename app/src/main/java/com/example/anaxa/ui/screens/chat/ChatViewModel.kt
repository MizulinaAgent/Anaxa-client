package com.example.anaxa.ui.screens.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anaxa.domain.model.Message
import com.example.anaxa.domain.repository.AuthRepository
import com.example.anaxa.domain.repository.MessagesRepository
import com.example.anaxa.ui.util.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<Message> = emptyList(),
    val currentUserId: String? = null,
    val draft: String = "",
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val messagesRepository: MessagesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: String = savedStateHandle.get<String>("orderId").orEmpty()

    var state by mutableStateOf(ChatUiState())
        private set

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val me = authRepository.getMe().getOrNull()
            messagesRepository.getMessages(orderId).fold(
                onSuccess = {
                    state = state.copy(isLoading = false, currentUserId = me?.id, messages = it)
                },
                onFailure = {
                    state = state.copy(isLoading = false, error = it.toUserMessage())
                }
            )
        }
    }

    fun onDraftChange(value: String) { state = state.copy(draft = value) }

    fun send() {
        val text = state.draft.trim()
        if (text.isBlank() || state.isSending) return
        viewModelScope.launch {
            state = state.copy(isSending = true)
            messagesRepository.sendMessage(orderId, text).fold(
                onSuccess = {
                    state = state.copy(
                        isSending = false,
                        draft = "",
                        messages = state.messages + it
                    )
                },
                onFailure = {
                    state = state.copy(isSending = false, error = it.toUserMessage())
                }
            )
        }
    }
}
