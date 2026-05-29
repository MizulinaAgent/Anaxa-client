package com.example.anaxa.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LotRequest(
    val categoryId: Int,
    val title: String,
    val description: String? = null,
    val price: Double,
    val quantity: Int = 1
)

@Serializable
data class LotUpdateRequest(
    val title: String? = null,
    val description: String? = null,
    val price: Double? = null,
    val quantity: Int? = null,
    val status: String? = null
)

@Serializable
data class LotDto(
    val id: String,
    val seller: UserDto,
    val categoryId: Int,
    val categoryType: String = "",
    val title: String,
    val description: String? = null,
    val price: Double,
    val quantity: Int = 1,
    val status: String,
    val createdAt: String
)
