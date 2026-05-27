package com.example.anaxa.domain.repository

import com.example.anaxa.domain.model.Message

interface MessagesRepository {
    suspend fun getMessages(orderId: String): Result<List<Message>>
    suspend fun sendMessage(orderId: String, content: String): Result<Message>
}
