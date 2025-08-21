package com.jparkbro.data.review

import com.jparkbro.model.common.ApiAction
import com.jparkbro.model.review.EditMyReviewRequest
import com.jparkbro.model.review.MyReview
import com.jparkbro.model.review.ReportReviewRequest
import com.jparkbro.network.review.ReviewDataSource
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewDataSource: ReviewDataSource
) : ReviewRepository {
    override suspend fun getMyReview(animeId: Int): Result<MyReview> {
        return reviewDataSource.getMyReview(animeId)
    }

    override suspend fun editMyReview(animeId: Int, request: EditMyReviewRequest): Result<Unit> {
        return reviewDataSource.editMyReview(animeId, request)
    }

    override suspend fun updateReviewLike(action: ApiAction, reviewId: Int): Result<Unit> {
        return when (action) {
            ApiAction.CREATE -> reviewDataSource.likedReview(reviewId)
            ApiAction.DELETE -> reviewDataSource.unLikedReview(reviewId)
            ApiAction.UPDATE -> Result.failure(IllegalArgumentException("좋아요는 UPDATE 액션을 지원하지 않습니다"))
        }
    }

    override suspend fun deleteReview(reviewId: Int): Result<Unit> {
        return reviewDataSource.deleteReview(reviewId)
    }

    override suspend fun reportReview(reviewId: Int, request: ReportReviewRequest): Result<Unit> {
        return reviewDataSource.reportReview(reviewId, request)
    }

    override suspend fun blockUser(userId: Int): Result<Unit> {
        return reviewDataSource.blockUser(userId)
    }
}