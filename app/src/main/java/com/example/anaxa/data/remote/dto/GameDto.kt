package com.example.anaxa.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GameDto(
    val id: Int,
    val name: String,
    val iconUrl: String? = null,
    val description: String? = null
)

@Serializable
data class CategoryDto(val id: Int, val gameId: Int, val type: String)
