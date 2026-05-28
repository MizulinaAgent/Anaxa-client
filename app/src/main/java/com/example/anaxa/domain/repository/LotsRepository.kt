package com.example.anaxa.domain.repository

import com.example.anaxa.domain.model.Lot

interface LotsRepository {
    suspend fun getLots(categoryId: Int?, status: String?, sellerId: String? = null): Result<List<Lot>>
    suspend fun getLot(id: String): Result<Lot>
    suspend fun createLot(categoryId: Int, title: String, description: String?, price: Double): Result<Lot>
    suspend fun updateLot(id: String, title: String?, description: String?, price: Double?, status: String?): Result<Lot>
    suspend fun deleteLot(id: String): Result<Unit>
}
