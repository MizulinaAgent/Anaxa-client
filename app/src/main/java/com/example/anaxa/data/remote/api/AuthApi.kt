package com.example.anaxa.data.remote.api

import com.example.anaxa.data.remote.dto.LoginRequest
import com.example.anaxa.data.remote.dto.LoginResponse
import com.example.anaxa.data.remote.dto.RegisterRequest
import com.example.anaxa.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): LoginResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun me(): UserDto

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): UserDto
}
