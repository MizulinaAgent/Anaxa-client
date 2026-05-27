package com.example.anaxa.domain.repository

import com.example.anaxa.domain.model.Review

interface ReviewsRepository {
    suspend fun createReview(orderId: String, revieweeId: String, rating: Int, comment: String?): Result<Review>
    suspend fun getUserReviews(userId: String): Result<List<Review>>
}
