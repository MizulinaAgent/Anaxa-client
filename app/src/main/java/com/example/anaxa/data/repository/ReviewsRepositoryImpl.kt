package com.example.anaxa.data.repository

import com.example.anaxa.data.mapper.toDomain
import com.example.anaxa.data.remote.api.ReviewsApi
import com.example.anaxa.data.remote.dto.ReviewRequest
import com.example.anaxa.domain.model.Review
import com.example.anaxa.domain.repository.ReviewsRepository
import javax.inject.Inject

class ReviewsRepositoryImpl @Inject constructor(
    private val api: ReviewsApi
) : ReviewsRepository {

    override suspend fun createReview(
        orderId: String,
        revieweeId: String,
        rating: Int,
        comment: String?
    ): Result<Review> = runCatching {
        api.createReview(ReviewRequest(orderId, revieweeId, rating, comment)).toDomain()
    }

    override suspend fun getUserReviews(userId: String): Result<List<Review>> =
        runCatching { api.getUserReviews(userId).map { it.toDomain() } }
}
