package com.example.anaxa.data.remote.api

import com.example.anaxa.data.remote.dto.OrderDto
import com.example.anaxa.data.remote.dto.OrderRequest
import com.example.anaxa.data.remote.dto.OrderStatusRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrdersApi {
    @POST("orders")
    suspend fun createOrder(@Body body: OrderRequest): OrderDto

    @GET("orders/{id}")
    suspend fun getOrder(@Path("id") id: String): OrderDto

    @GET("orders/my")
    suspend fun getMyOrders(@Query("role") role: String): List<OrderDto>

    @PUT("orders/{id}/status")
    suspend fun updateStatus(@Path("id") id: String, @Body body: OrderStatusRequest): OrderDto
}
