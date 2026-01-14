package com.jparkbro.network.review

import com.jparkbro.model.common.review.AnimeDetailMyReviewDto
import com.jparkbro.model.common.review.ReviewFormAnimeReviewDto
import com.jparkbro.model.dto.info.GetInfoReviewsRequest
import com.jparkbro.model.dto.info.GetInfoReviewsResponse
import com.jparkbro.model.dto.info.ReviewRatingRequest
import com.jparkbro.model.dto.review.SaveMyReviewRequest
import com.jparkbro.model.review.EditMyReviewRequest
import com.jparkbro.model.review.MyReview
import com.jparkbro.model.review.ReportReviewRequest

interface ReviewDataSource {
    /** Anime detail My Review */
    suspend fun getAnimeDetailMyReview(animeId: Long): Result<AnimeDetailMyReviewDto>
    suspend fun createAnimeRating(animeId: Long, request: ReviewRatingRequest): Result<Unit>
    suspend fun updateAnimeRating(reviewId: Long, request: ReviewRatingRequest): Result<Unit>
    suspend fun deleteAnimeRating(reviewId: Long): Result<Unit>

    /** Anime detail Reviews */
    suspend fun getAnimeDetailReviews(request: GetInfoReviewsRequest): Result<GetInfoReviewsResponse>

    /** Review Form */
    suspend fun getReviewFormAnimeReview(animeId: Long): Result<ReviewFormAnimeReviewDto>
    suspend fun updateMyReview(animeId: Long, request: SaveMyReviewRequest): Result<Unit>


    suspend fun getMyReview(animeId: Long): Result<MyReview>

    suspend fun editMyReview(animeId: Long, request: EditMyReviewRequest): Result<Unit>

    suspend fun likedReview(reviewId: Long): Result<Unit>
    suspend fun unLikedReview(reviewId: Long): Result<Unit>

    suspend fun deleteReview(reviewId: Long): Result<Unit>

    suspend fun reportReview(reviewId: Long, request: ReportReviewRequest): Result<Unit>
    suspend fun blockUser(userId: Long): Result<Unit>
}