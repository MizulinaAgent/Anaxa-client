package com.example.anaxa.ui.screens.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anaxa.domain.model.Message
import com.example.anaxa.domain.model.Order
import com.example.anaxa.domain.repository.AuthRepository
import com.example.anaxa.domain.repository.MessagesRepository
import com.example.anaxa.domain.repository.OrdersRepository
import com.example.anaxa.domain.repository.ReviewsRepository
import com.example.anaxa.ui.util.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val order: Order? = null,
    val messages: List<Message> = emptyList(),
    val currentUserId: String? = null,
    val draft: String = "",
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val isCompleting: Boolean = false,
    val isRefunding: Boolean = false,
    val isSubmittingReview: Boolean = false,
    val reviewSubmitted: Boolean = false,
    val error: String? = null
) {
    val isOwnSeller: Boolean get() = order != null && order.seller.id == currentUserId
    val isOwnBuyer: Boolean get() = order != null && order.buyer.id == currentUserId
    val canComplete: Boolean get() = isOwnBuyer && order?.status != "completed" && order?.status != "cancelled"
    val canReview: Boolean get() = isOwnBuyer && order?.status == "completed" && !reviewSubmitted
    val canRefund: Boolean get() = isOwnSeller && order?.status != "cancelled"
}

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val messagesRepository: MessagesRepository,
    private val ordersRepository: OrdersRepository,
    private val reviewsRepository: ReviewsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: String = savedStateHandle.get<String>("orderId").orEmpty()

    var state by mutableStateOf(ChatUiState())
        private set

    init {
        load()
        startPolling()
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (true) {
                delay(4000)
                if (!state.isLoading && !state.isSending) {
                    messagesRepository.getMessages(orderId).onSuccess {
                        if (it.size != state.messages.size) {
                            state = state.copy(messages = it)
                        }
                    }
                }
            }
        }
    }

    fun load() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            val me = authRepository.getMe().getOrNull()
            val order = ordersRepository.getOrder(orderId).getOrNull()
            val messagesResult = messagesRepository.getMessages(orderId)

            val alreadyReviewed = if (order != null && me != null && order.buyer.id == me.id) {
                reviewsRepository.getUserReviews(order.seller.id)
                    .getOrNull()
                    ?.any { it.orderId == orderId && it.reviewer.id == me.id }
                    ?: false
            } else false

            messagesResult.fold(
                onSuccess = {
                    state = state.copy(
                        isLoading = false,
                        currentUserId = me?.id,
                        order = order,
                        messages = it,
                        reviewSubmitted = alreadyReviewed
                    )
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

    fun completeOrder() {
        if (state.isCompleting) return
        viewModelScope.launch {
            state = state.copy(isCompleting = true, error = null)
            ordersRepository.updateStatus(orderId, "completed").fold(
                onSuccess = { state = state.copy(isCompleting = false, order = it) },
                onFailure = { state = state.copy(isCompleting = false, error = it.toUserMessage()) }
            )
        }
    }

    fun refund() {
        if (state.isRefunding) return
        viewModelScope.launch {
            state = state.copy(isRefunding = true, error = null)
            ordersRepository.refund(orderId).fold(
                onSuccess = { state = state.copy(isRefunding = false, order = it) },
                onFailure = { state = state.copy(isRefunding = false, error = it.toUserMessage()) }
            )
        }
    }

    fun submitReview(rating: Int, comment: String) {
        val order = state.order ?: return
        if (state.isSubmittingReview || rating !in 1..5) return
        viewModelScope.launch {
            state = state.copy(isSubmittingReview = true, error = null)
            reviewsRepository.createReview(
                orderId = orderId,
                revieweeId = order.seller.id,
                rating = rating,
                comment = comment.trim().ifBlank { null }
            ).fold(
                onSuccess = { state = state.copy(isSubmittingReview = false, reviewSubmitted = true) },
                onFailure = { state = state.copy(isSubmittingReview = false, error = it.toUserMessage()) }
            )
        }
    }
}
