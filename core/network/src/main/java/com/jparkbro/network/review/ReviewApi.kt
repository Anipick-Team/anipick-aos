package com.jparkbro.network.review

import com.jparkbro.model.review.EditMyReviewRequest
import com.jparkbro.model.review.MyReview
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewApi {
    @GET(ApiConstants.GET_MY_REVIEW)
    suspend fun getMyReview(
        @Path("animeId") animeId: Int
    ): Response<ApiResponse<MyReview>>

    @PATCH(ApiConstants.EDIT_MY_REVEIW)
    suspend fun editMyReview(
        @Path("animeId") animeId: Int,
        @Body request: EditMyReviewRequest
    ): Response<ApiResponse<Unit>>

    @POST(ApiConstants.LIKED_REVIEW)
    suspend fun likedReview(
        @Path("reviewId") reviewId: Int
    ): Response<ApiResponse<Unit>>

    @DELETE(ApiConstants.UN_LIKED_REVIEW)
    suspend fun unLikedReview(
        @Path("reviewId") reviewId: Int
    ): Response<ApiResponse<Unit>>

    @DELETE(ApiConstants.DELETE_REVIEW)
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Int
    ): Response<ApiResponse<Unit>>

    @POST(ApiConstants.REPORT_REVIEW)
    suspend fun reportReview(
        @Path("reviewId") reviewId: Int
    ): Response<ApiResponse<Unit>>

    @POST(ApiConstants.BLOCK_USER)
    suspend fun blockUser(
        @Path("userId") userId: Int
    ): Response<ApiResponse<Unit>>
}