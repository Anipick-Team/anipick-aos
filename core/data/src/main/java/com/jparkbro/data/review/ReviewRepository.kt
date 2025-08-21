package com.jparkbro.data.review

import com.jparkbro.model.common.ApiAction
import com.jparkbro.model.review.EditMyReviewRequest
import com.jparkbro.model.review.MyReview
import com.jparkbro.model.review.ReportReviewRequest

interface ReviewRepository {
    suspend fun getMyReview(animeId: Int): Result<MyReview>

    suspend fun editMyReview(animeId: Int, request: EditMyReviewRequest): Result<Unit>

    suspend fun updateReviewLike(action: ApiAction, reviewId: Int): Result<Unit>

    suspend fun deleteReview(reviewId: Int): Result<Unit>

    suspend fun reportReview(reviewId: Int, request: ReportReviewRequest): Result<Unit>
    suspend fun blockUser(userId: Int): Result<Unit>
}