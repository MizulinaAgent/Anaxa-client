package com.example.anaxa.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LotRequest(
    val categoryId: Int,
    val title: String,
    val description: String? = null,
    val price: Double
)

@Serializable
data class LotUpdateRequest(
    val title: String? = null,
    val description: String? = null,
    val price: Double? = null,
    val status: String? = null
)

@Serializable
data class LotDto(
    val id: String,
    val seller: UserDto,
    val categoryId: Int,
    val title: String,
    val description: String? = null,
    val price: Double,
    val status: String,
    val createdAt: String
)
