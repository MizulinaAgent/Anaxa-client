package com.example.anaxa.domain.model

data class Message(
    val id: String,
    val orderId: String,
    val sender: User,
    val content: String,
    val createdAt: String
)

data class Review(
    val id: String,
    val orderId: String,
    val reviewer: User,
    val reviewee: User,
    val rating: Int,
    val comment: String?,
    val createdAt: String
)
