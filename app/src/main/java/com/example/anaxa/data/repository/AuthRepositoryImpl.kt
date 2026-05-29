package com.example.anaxa.data.repository

import com.example.anaxa.data.local.TokenDataStore
import com.example.anaxa.data.mapper.toDomain
import com.example.anaxa.data.remote.api.AuthApi
import com.example.anaxa.data.remote.dto.LoginRequest
import com.example.anaxa.data.remote.dto.RegisterRequest
import com.example.anaxa.domain.model.User
import com.example.anaxa.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenDataStore: TokenDataStore
) : AuthRepository {

    override suspend fun register(email: String, password: String, username: String): Result<User> =
        runCatching {
            val response = api.register(RegisterRequest(email, password, username))
            tokenDataStore.saveToken(response.token)
            response.user.toDomain()
        }

    override suspend fun login(email: String, password: String): Result<User> =
        runCatching {
            val response = api.login(LoginRequest(email, password))
            tokenDataStore.saveToken(response.token)
            response.user.toDomain()
        }

    override suspend fun getMe(): Result<User> = runCatching { api.me().toDomain() }

    override suspend fun getUser(userId: String): Result<User> =
        runCatching { api.getUser(userId).toDomain() }

    override suspend fun logout() {
        tokenDataStore.clear()
    }

    override val isLoggedIn: Flow<Boolean> =
        tokenDataStore.token.map { !it.isNullOrBlank() }
}
