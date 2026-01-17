package com.jparkbro.network.review

import com.jparkbro.model.common.review.AnimeDetailMyReviewDto
import com.jparkbro.model.common.review.ReviewFormAnimeReviewDto
import com.jparkbro.model.dto.info.GetInfoReviewsResponse
import com.jparkbro.model.dto.info.ReviewRatingRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResponse
import com.jparkbro.model.dto.review.SaveMyReviewRequest
import com.jparkbro.model.mypage.MyReviewsResponse
import com.jparkbro.model.review.EditMyReviewRequest
import com.jparkbro.model.review.MyReview
import com.jparkbro.model.review.ReportReviewRequest
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewApi {
    @GET(ApiConstants.GET_DETAIL_MY_REVIEW)
    suspend fun getAnimeDetailMyReview(
        @Path("animeId") animeId: Long
    ): Response<ApiResponse<AnimeDetailMyReviewDto>>

    @POST(ApiConstants.CREATE_ANIME_RATING)
    suspend fun createAnimeRating(
        @Path("animeId") animeId: Long,
        @Body request: ReviewRatingRequest
    ): Response<ApiResponse<Unit>>

    @PATCH(ApiConstants.UPDATE_ANIME_RATING)
    suspend fun updateAnimeRating(
        @Path("reviewId") reviewId: Long,
        @Body request: ReviewRatingRequest
    ): Response<ApiResponse<Unit>>

    @DELETE(ApiConstants.DELETE_ANIME_RATING)
    suspend fun deleteAnimeRating(
        @Path("reviewId") reviewId: Long
    ): Response<ApiResponse<Unit>>

    @GET(ApiConstants.GET_DETAIL_REVIEWS)
    suspend fun getAnimeDetailReviews(
        @Path("animeId") animeId: Long,
        @Query("sort") sort: String,
        @Query("isSpoiler") isSpoiler: Boolean,
        @Query("lastValue") lastValue: String?,
        @Query("lastId") lastId: Long?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<GetInfoReviewsResponse>>

    @GET(ApiConstants.GET_MY_REVIEW)
    suspend fun getReviewFormAnimeReview(
        @Path("animeId") animeId: Long
    ): Response<ApiResponse<ReviewFormAnimeReviewDto>>

    @PATCH(ApiConstants.EDIT_MY_REVEIW)
    suspend fun updateMyReview(
        @Path("animeId") animeId: Long,
        @Body request: SaveMyReviewRequest
    ): Response<ApiResponse<Unit>>

    @GET(ApiConstants.MY_RATED_REVIEWS)
    suspend fun loadUserContentReviews(
        @Query("lastId") lastId: Long?,
        @Query("lastLikeCount") lastLikeCount: String?,
        @Query("lastRating") lastRating: String?,
        @Query("sort") sort: String,
        @Query("reviewOnly") reviewOnly: Boolean,
        @Query("size") size: Int?
    ): Response<ApiResponse<GetUserContentResponse>>






    @GET(ApiConstants.GET_MY_REVIEW)
    suspend fun getMyReview(
        @Path("animeId") animeId: Long
    ): Response<ApiResponse<MyReview>>

    @PATCH(ApiConstants.EDIT_MY_REVEIW)
    suspend fun editMyReview(
        @Path("animeId") animeId: Long,
        @Body request: EditMyReviewRequest
    ): Response<ApiResponse<Unit>>

    @POST(ApiConstants.LIKED_REVIEW)
    suspend fun likedReview(
        @Path("reviewId") reviewId: Long
    ): Response<ApiResponse<Unit>>

    @DELETE(ApiConstants.UN_LIKED_REVIEW)
    suspend fun unLikedReview(
        @Path("reviewId") reviewId: Long
    ): Response<ApiResponse<Unit>>

    @DELETE(ApiConstants.DELETE_REVIEW)
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Long
    ): Response<ApiResponse<Unit>>

    @POST(ApiConstants.REPORT_REVIEW)
    suspend fun reportReview(
        @Path("reviewId") reviewId: Long,
        @Body request: ReportReviewRequest
    ): Response<ApiResponse<Unit>>

    @POST(ApiConstants.BLOCK_USER)
    suspend fun blockUser(
        @Path("userId") userId: Long
    ): Response<ApiResponse<Unit>>
}