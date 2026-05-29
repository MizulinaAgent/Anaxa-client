package com.example.anaxa.domain.model

data class Order(
    val id: String,
    val lot: Lot,
    val buyer: User,
    val seller: User,
    val quantity: Int,
    val status: String,
    val createdAt: String
)
