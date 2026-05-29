package com.example.anaxa.domain.model

data class Lot(
    val id: String,
    val seller: User,
    val categoryId: Int,
    val categoryType: String,
    val title: String,
    val description: String?,
    val price: Double,
    val quantity: Int,
    val status: String,
    val createdAt: String
)
