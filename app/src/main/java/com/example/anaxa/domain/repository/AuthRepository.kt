package com.example.anaxa.domain.repository

import com.example.anaxa.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(email: String, password: String, username: String): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun getMe(): Result<User>
    suspend fun getUser(userId: String): Result<User>
    suspend fun logout()
    val isLoggedIn: Flow<Boolean>
}
