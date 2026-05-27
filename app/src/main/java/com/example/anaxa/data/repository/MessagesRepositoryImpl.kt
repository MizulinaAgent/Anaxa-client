package com.example.anaxa.data.repository

import com.example.anaxa.data.mapper.toDomain
import com.example.anaxa.data.remote.api.MessagesApi
import com.example.anaxa.data.remote.dto.MessageRequest
import com.example.anaxa.domain.model.Message
import com.example.anaxa.domain.repository.MessagesRepository
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    private val api: MessagesApi
) : MessagesRepository {

    override suspend fun getMessages(orderId: String): Result<List<Message>> =
        runCatching { api.getMessages(orderId).map { it.toDomain() } }

    override suspend fun sendMessage(orderId: String, content: String): Result<Message> =
        runCatching { api.sendMessage(orderId, MessageRequest(content)).toDomain() }
}
