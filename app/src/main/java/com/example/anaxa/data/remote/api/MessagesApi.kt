package com.example.anaxa.data.remote.api

import com.example.anaxa.data.remote.dto.MessageDto
import com.example.anaxa.data.remote.dto.MessageRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessagesApi {
    @GET("orders/{orderId}/messages")
    suspend fun getMessages(@Path("orderId") orderId: String): List<MessageDto>

    @POST("orders/{orderId}/messages")
    suspend fun sendMessage(@Path("orderId") orderId: String, @Body body: MessageRequest): MessageDto
}
