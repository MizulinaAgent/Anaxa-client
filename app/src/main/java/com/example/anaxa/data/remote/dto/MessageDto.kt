package com.example.anaxa.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessageRequest(val content: String)

@Serializable
data class MessageDto(
    val id: String,
    val orderId: String,
    val sender: UserDto,
    val content: String,
    val createdAt: String
)

@Serializable
data class ReviewRequest(
    val orderId: String,
    val revieweeId: String,
    val rating: Int,
    val comment: String? = null
)

@Serializable
data class ReviewDto(
    val id: String,
    val orderId: String,
    val reviewer: UserDto,
    val reviewee: UserDto,
    val rating: Int,
    val comment: String? = null,
    val createdAt: String
)
