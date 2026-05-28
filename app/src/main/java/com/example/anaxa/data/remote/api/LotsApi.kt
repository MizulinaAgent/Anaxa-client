package com.example.anaxa.data.remote.api

import com.example.anaxa.data.remote.dto.LotDto
import com.example.anaxa.data.remote.dto.LotRequest
import com.example.anaxa.data.remote.dto.LotUpdateRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LotsApi {
    @GET("lots")
    suspend fun getLots(
        @Query("categoryId") categoryId: Int? = null,
        @Query("status") status: String? = null,
        @Query("sellerId") sellerId: String? = null
    ): List<LotDto>

    @GET("lots/{id}")
    suspend fun getLot(@Path("id") id: String): LotDto

    @POST("lots")
    suspend fun createLot(@Body body: LotRequest): LotDto

    @PUT("lots/{id}")
    suspend fun updateLot(@Path("id") id: String, @Body body: LotUpdateRequest): LotDto

    @DELETE("lots/{id}")
    suspend fun deleteLot(@Path("id") id: String)
}
