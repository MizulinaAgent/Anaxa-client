package com.example.anaxa.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(val lotId: String)

@Serializable
data class OrderStatusRequest(val status: String)

@Serializable
data class OrderDto(
    val id: String,
    val lot: LotDto,
    val buyer: UserDto,
    val seller: UserDto,
    val status: String,
    val createdAt: String
)
