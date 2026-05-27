package com.example.anaxa.data.remote.api

import com.example.anaxa.data.remote.dto.ReviewDto
import com.example.anaxa.data.remote.dto.ReviewRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewsApi {
    @POST("reviews")
    suspend fun createReview(@Body body: ReviewRequest): ReviewDto

    @GET("users/{userId}/reviews")
    suspend fun getUserReviews(@Path("userId") userId: String): List<ReviewDto>
}
