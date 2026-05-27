package com.example.anaxa.data.repository

import com.example.anaxa.data.mapper.toDomain
import com.example.anaxa.data.remote.api.GamesApi
import com.example.anaxa.domain.model.Category
import com.example.anaxa.domain.model.Game
import com.example.anaxa.domain.repository.GamesRepository
import javax.inject.Inject

class GamesRepositoryImpl @Inject constructor(
    private val api: GamesApi
) : GamesRepository {

    override suspend fun getGames(): Result<List<Game>> =
        runCatching { api.getGames().map { it.toDomain() } }

    override suspend fun getGame(id: Int): Result<Game> =
        runCatching { api.getGame(id).toDomain() }

    override suspend fun getCategories(gameId: Int): Result<List<Category>> =
        runCatching { api.getCategories(gameId).map { it.toDomain() } }
}
