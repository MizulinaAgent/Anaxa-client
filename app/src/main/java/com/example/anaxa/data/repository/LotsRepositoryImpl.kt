package com.example.anaxa.data.repository

import com.example.anaxa.data.mapper.toDomain
import com.example.anaxa.data.remote.api.LotsApi
import com.example.anaxa.data.remote.dto.LotRequest
import com.example.anaxa.data.remote.dto.LotUpdateRequest
import com.example.anaxa.domain.model.Lot
import com.example.anaxa.domain.repository.LotsRepository
import javax.inject.Inject

class LotsRepositoryImpl @Inject constructor(
    private val api: LotsApi
) : LotsRepository {

    override suspend fun getLots(categoryId: Int?, status: String?): Result<List<Lot>> =
        runCatching { api.getLots(categoryId, status).map { it.toDomain() } }

    override suspend fun getLot(id: String): Result<Lot> =
        runCatching { api.getLot(id).toDomain() }

    override suspend fun createLot(
        categoryId: Int,
        title: String,
        description: String?,
        price: Double
    ): Result<Lot> = runCatching {
        api.createLot(LotRequest(categoryId, title, description, price)).toDomain()
    }

    override suspend fun updateLot(
        id: String,
        title: String?,
        description: String?,
        price: Double?,
        status: String?
    ): Result<Lot> = runCatching {
        api.updateLot(id, LotUpdateRequest(title, description, price, status)).toDomain()
    }

    override suspend fun deleteLot(id: String): Result<Unit> =
        runCatching { api.deleteLot(id) }
}
