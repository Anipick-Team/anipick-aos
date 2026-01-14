package com.jparkbro.network.review

import com.jparkbro.model.common.review.AnimeDetailMyReviewDto
import com.jparkbro.model.common.review.ReviewFormAnimeReviewDto
import com.jparkbro.model.dto.info.GetInfoReviewsRequest
import com.jparkbro.model.dto.info.GetInfoReviewsResponse
import com.jparkbro.model.dto.info.ReviewRatingRequest
import com.jparkbro.model.dto.review.SaveMyReviewRequest
import com.jparkbro.model.enum.ReviewSortType
import com.jparkbro.model.review.EditMyReviewRequest
import com.jparkbro.model.review.MyReview
import com.jparkbro.model.review.ReportReviewRequest
import com.jparkbro.network.detail.RetrofitDetailDataSource
import com.jparkbro.network.util.toResult
import com.jparkbro.network.util.toUnitResult
import javax.inject.Inject

class RetrofitReviewDataSource @Inject constructor(
    private val reviewApi: ReviewApi
) : ReviewDataSource {
    companion object {
        const val TAG = "RetrofitReviewDataSource"
    }

    override suspend fun getAnimeDetailMyReview(animeId: Long): Result<AnimeDetailMyReviewDto> {
        return reviewApi.getAnimeDetailMyReview(animeId).toResult(TAG, "getAnimeDetailMyReview")
    }

    override suspend fun createAnimeRating(animeId: Long, request: ReviewRatingRequest): Result<Unit> {
        return reviewApi.createAnimeRating(animeId, request).toUnitResult(TAG, "createAnimeRating")
    }

    override suspend fun updateAnimeRating(reviewId: Long, request: ReviewRatingRequest): Result<Unit> {
        return reviewApi.updateAnimeRating(reviewId, request).toUnitResult(TAG, "updateAnimeRating")
    }

    override suspend fun deleteAnimeRating(reviewId: Long): Result<Unit> {
        return reviewApi.deleteAnimeRating(reviewId).toUnitResult(TAG, "deleteAnimeRating")
    }

    override suspend fun getAnimeDetailReviews(request: GetInfoReviewsRequest): Result<GetInfoReviewsResponse> {
        return reviewApi.getAnimeDetailReviews(
            animeId = request.animeId,
            sort = request.sort,
            isSpoiler = request.isSpoiler,
            lastValue = request.lastValue,
            lastId = request.lastId,
            size = request.size
        ).toResult(TAG, "getAnimeDetailReviews")
    }

    override suspend fun getReviewFormAnimeReview(animeId: Long): Result<ReviewFormAnimeReviewDto> {
        return reviewApi.getReviewFormAnimeReview(animeId).toResult(TAG, "getReviewFormAnimeReview")
    }

    override suspend fun updateMyReview(animeId: Long, request: SaveMyReviewRequest): Result<Unit> {
        return reviewApi.updateMyReview(animeId, request).toUnitResult(TAG, "updateMyReview")
    }







    override suspend fun getMyReview(animeId: Long): Result<MyReview> {
        return reviewApi.getMyReview(animeId).toResult(TAG, "getMyReview")
    }

    override suspend fun editMyReview(animeId: Long, request: EditMyReviewRequest): Result<Unit> {
        return reviewApi.editMyReview(animeId, request).toUnitResult(TAG, "editMyReview")
    }

    override suspend fun likedReview(reviewId: Long): Result<Unit> {
        return reviewApi.likedReview(reviewId).toUnitResult(TAG, "likedReview")
    }

    override suspend fun unLikedReview(reviewId: Long): Result<Unit> {
        return reviewApi.unLikedReview(reviewId).toUnitResult(TAG, "unLikedReview")
    }

    override suspend fun deleteReview(reviewId: Long): Result<Unit> {
        return reviewApi.deleteReview(reviewId).toUnitResult(TAG, "deleteReview")
    }

    override suspend fun reportReview(reviewId: Long, request: ReportReviewRequest): Result<Unit> {
        return reviewApi.reportReview(
            reviewId = reviewId,
            request = request
        ).toUnitResult(TAG, "reportReview")
    }

    override suspend fun blockUser(userId: Long): Result<Unit> {
        return reviewApi.blockUser(userId).toUnitResult(TAG, "blockUser")
    }
}