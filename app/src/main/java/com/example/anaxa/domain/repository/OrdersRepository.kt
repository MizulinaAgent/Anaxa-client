package com.example.anaxa.domain.repository

import com.example.anaxa.domain.model.Order

interface OrdersRepository {
    suspend fun createOrder(lotId: String, quantity: Int): Result<Order>
    suspend fun getOrder(id: String): Result<Order>
    suspend fun getMyOrders(role: String): Result<List<Order>>
    suspend fun updateStatus(id: String, status: String): Result<Order>
}
