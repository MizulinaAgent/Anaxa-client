package com.example.anaxa.data.remote.api

import com.example.anaxa.data.remote.dto.CategoryDto
import com.example.anaxa.data.remote.dto.GameDto
import retrofit2.http.GET
import retrofit2.http.Path

interface GamesApi {
    @GET("games")
    suspend fun getGames(): List<GameDto>

    @GET("games/{id}")
    suspend fun getGame(@Path("id") id: Int): GameDto

    @GET("games/{gameId}/categories")
    suspend fun getCategories(@Path("gameId") gameId: Int): List<CategoryDto>
}
