package com.example.anaxa.data.repository

import com.example.anaxa.data.mapper.toDomain
import com.example.anaxa.data.remote.api.OrdersApi
import com.example.anaxa.data.remote.dto.OrderRequest
import com.example.anaxa.data.remote.dto.OrderStatusRequest
import com.example.anaxa.domain.model.Order
import com.example.anaxa.domain.repository.OrdersRepository
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val api: OrdersApi
) : OrdersRepository {

    override suspend fun createOrder(lotId: String, quantity: Int): Result<Order> =
        runCatching { api.createOrder(OrderRequest(lotId, quantity)).toDomain() }

    override suspend fun getOrder(id: String): Result<Order> =
        runCatching { api.getOrder(id).toDomain() }

    override suspend fun getMyOrders(role: String): Result<List<Order>> =
        runCatching { api.getMyOrders(role).map { it.toDomain() } }

    override suspend fun updateStatus(id: String, status: String): Result<Order> =
        runCatching { api.updateStatus(id, OrderStatusRequest(status)).toDomain() }
}
