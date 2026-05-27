package com.example.anaxa.domain.model

data class Lot(
    val id: String,
    val seller: User,
    val categoryId: Int,
    val title: String,
    val description: String?,
    val price: Double,
    val status: String,
    val createdAt: String
)
