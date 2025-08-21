package com.jparkbro.network.review

import com.jparkbro.model.common.ApiAction
import com.jparkbro.model.review.EditMyReviewRequest
import com.jparkbro.model.review.MyReview
import com.jparkbro.model.review.ReportReviewRequest
import com.jparkbro.network.util.toResult
import com.jparkbro.network.util.toUnitResult
import javax.inject.Inject

class RetrofitReviewDataSource @Inject constructor(
    private val reviewApi: ReviewApi
) : ReviewDataSource {
    companion object {
        const val TAG = "RetrofitReviewDataSource"
    }

    override suspend fun getMyReview(animeId: Int): Result<MyReview> {
        return reviewApi.getMyReview(animeId).toResult(TAG, "getMyReview")
    }

    override suspend fun editMyReview(animeId: Int, request: EditMyReviewRequest): Result<Unit> {
        return reviewApi.editMyReview(animeId, request).toUnitResult(TAG, "editMyReview")
    }

    override suspend fun likedReview(reviewId: Int): Result<Unit> {
        return reviewApi.likedReview(reviewId).toUnitResult(TAG, "likedReview")
    }

    override suspend fun unLikedReview(reviewId: Int): Result<Unit> {
        return reviewApi.unLikedReview(reviewId).toUnitResult(TAG, "unLikedReview")
    }

    override suspend fun deleteReview(reviewId: Int): Result<Unit> {
        return reviewApi.deleteReview(reviewId).toUnitResult(TAG, "deleteReview")
    }

    override suspend fun reportReview(reviewId: Int, request: ReportReviewRequest): Result<Unit> {
        return reviewApi.reportReview(
            reviewId = reviewId,
            request = request
        ).toUnitResult(TAG, "reportReview")
    }

    override suspend fun blockUser(userId: Int): Result<Unit> {
        return reviewApi.blockUser(userId).toUnitResult(TAG, "blockUser")
    }
}