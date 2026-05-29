package com.example.anaxa.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(val lotId: String, val quantity: Int = 1)

@Serializable
data class OrderStatusRequest(val status: String)

@Serializable
data class OrderDto(
    val id: String,
    val lot: LotDto,
    val buyer: UserDto,
    val seller: UserDto,
    val quantity: Int = 1,
    val status: String,
    val unreadCount: Int = 0,
    val createdAt: String
)
