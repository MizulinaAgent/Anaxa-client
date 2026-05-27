package com.example.anaxa.domain.model

data class Game(
    val id: Int,
    val name: String,
    val iconUrl: String?,
    val description: String?
)

data class Category(val id: Int, val gameId: Int, val type: String)
