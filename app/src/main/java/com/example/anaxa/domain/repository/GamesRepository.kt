package com.example.anaxa.domain.repository

import com.example.anaxa.domain.model.Category
import com.example.anaxa.domain.model.Game

interface GamesRepository {
    suspend fun getGames(): Result<List<Game>>
    suspend fun getGame(id: Int): Result<Game>
    suspend fun getCategories(gameId: Int): Result<List<Category>>
}
