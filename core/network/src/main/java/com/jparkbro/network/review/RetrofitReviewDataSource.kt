package com.jparkbro.network.review

import com.jparkbro.model.common.review.AnimeDetailMyReviewDto
import com.jparkbro.model.common.review.ReviewFormAnimeReviewDto
import com.jparkbro.model.dto.info.GetInfoReviewsRequest
import com.jparkbro.model.dto.info.GetInfoReviewsResponse
import com.jparkbro.model.dto.info.ReviewRatingRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResponse
import com.jparkbro.model.dto.review.SaveMyReviewRequest
import com.jparkbro.model.review.ReportReviewRequest
import com.jparkbro.network.util.safeApiCall
import com.jparkbro.network.util.safeApiCallUnit
import javax.inject.Inject

class RetrofitReviewDataSource @Inject constructor(
    private val reviewApi: ReviewApi
) : ReviewDataSource {
    companion object {
        const val TAG = "RetrofitReviewDataSource"
    }

    override suspend fun getAnimeDetailMyReview(animeId: Long): Result<AnimeDetailMyReviewDto> {
        return safeApiCall(TAG, "getAnimeDetailMyReview") { reviewApi.getAnimeDetailMyReview(animeId) }
    }

    override suspend fun createAnimeRating(animeId: Long, request: ReviewRatingRequest): Result<Unit> {
        return safeApiCallUnit(TAG, "createAnimeRating") { reviewApi.createAnimeRating(animeId, request) }
    }

    override suspend fun updateAnimeRating(reviewId: Long, request: ReviewRatingRequest): Result<Unit> {
        return safeApiCallUnit(TAG, "updateAnimeRating") { reviewApi.updateAnimeRating(reviewId, request) }
    }

    override suspend fun deleteAnimeRating(reviewId: Long): Result<Unit> {
        return safeApiCallUnit(TAG, "deleteAnimeRating") { reviewApi.deleteAnimeRating(reviewId) }
    }

    override suspend fun getAnimeDetailReviews(request: GetInfoReviewsRequest): Result<GetInfoReviewsResponse> {
        return safeApiCall(TAG, "getAnimeDetailReviews") {
            reviewApi.getAnimeDetailReviews(
                animeId = request.animeId,
                sort = request.sort,
                isSpoiler = request.isSpoiler,
                lastValue = request.lastValue,
                lastId = request.lastId,
                size = request.size
            )
        }
    }

    override suspend fun getReviewFormAnimeReview(animeId: Long): Result<ReviewFormAnimeReviewDto> {
        return safeApiCall(TAG, "getReviewFormAnimeReview") { reviewApi.getReviewFormAnimeReview(animeId) }
    }

    override suspend fun updateMyReview(animeId: Long, request: SaveMyReviewRequest): Result<Unit> {
        return safeApiCallUnit(TAG, "updateMyReview") { reviewApi.updateMyReview(animeId, request) }
    }

    override suspend fun loadUserContentReviews(request: GetUserContentRequest): Result<GetUserContentResponse> {
        return safeApiCall(TAG, "loadUserContentReviews") {
            reviewApi.loadUserContentReviews(
                lastId = request.lastId,
                lastLikeCount = request.lastLikeCount,
                lastRating = request.lastRating,
                sort = request.sort.param,
                reviewOnly = request.reviewOnly,
                size = request.size
            )
        }
    }

    override suspend fun likedReview(reviewId: Long): Result<Unit> {
        return safeApiCallUnit(TAG, "likedReview") { reviewApi.likedReview(reviewId) }
    }

    override suspend fun unLikedReview(reviewId: Long): Result<Unit> {
        return safeApiCallUnit(TAG, "unLikedReview") { reviewApi.unLikedReview(reviewId) }
    }

    override suspend fun deleteReview(reviewId: Long): Result<Unit> {
        return safeApiCallUnit(TAG, "deleteReview") { reviewApi.deleteReview(reviewId) }
    }

    override suspend fun reportReview(reviewId: Long, request: ReportReviewRequest): Result<Unit> {
        return safeApiCallUnit(TAG, "reportReview") {
            reviewApi.reportReview(
                reviewId = reviewId,
                request = request
            )
        }
    }

    override suspend fun blockUser(userId: Long): Result<Unit> {
        return safeApiCallUnit(TAG, "blockUser") { reviewApi.blockUser(userId) }
    }
}
