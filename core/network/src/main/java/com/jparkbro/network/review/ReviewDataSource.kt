package com.jparkbro.network.review

import com.jparkbro.model.review.EditMyReviewRequest
import com.jparkbro.model.review.MyReview
import com.jparkbro.model.review.ReportReviewRequest

interface ReviewDataSource {
    suspend fun getMyReview(animeId: Int): Result<MyReview>

    suspend fun editMyReview(animeId: Int, request: EditMyReviewRequest): Result<Unit>

    suspend fun likedReview(reviewId: Int): Result<Unit>
    suspend fun unLikedReview(reviewId: Int): Result<Unit>

    suspend fun deleteReview(reviewId: Int): Result<Unit>

    suspend fun reportReview(reviewId: Int, request: ReportReviewRequest): Result<Unit>
    suspend fun blockUser(userId: Int): Result<Unit>
}