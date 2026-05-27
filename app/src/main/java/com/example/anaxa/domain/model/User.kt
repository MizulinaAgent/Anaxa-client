package com.example.anaxa.domain.model

data class User(
    val id: String,
    val email: String,
    val username: String,
    val avatarUrl: String?,
    val rating: Double,
    val createdAt: String
)
