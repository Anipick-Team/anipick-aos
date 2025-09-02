package com.jparkbro.network.detail

import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.detail.ActorDetailResponse
import com.jparkbro.model.detail.AnimeActorsResponse
import com.jparkbro.model.detail.DetailActor
import com.jparkbro.model.detail.DetailInfo
import com.jparkbro.model.detail.DetailMyReview
import com.jparkbro.model.detail.DetailSeries
import com.jparkbro.model.detail.DetailStudio
import com.jparkbro.model.detail.ReviewDetailResponse
import com.jparkbro.model.detail.WatchStatusRequest
import com.jparkbro.model.review.ReviewRating
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

interface DetailApi {
    @GET(ApiConstants.GET_DETAIL_INFO)
    suspend fun getDetailInfo(
        @Path("animeId") animeId: Int,
    ): Response<ApiResponse<DetailInfo>>

    @GET(ApiConstants.GET_DETAIL_ACTOR)
    suspend fun getDetailActor(
        @Path("animeId") animeId: Int,
    ): Response<ApiResponse<List<DetailActor>>>

    @GET(ApiConstants.GET_DETAIL_SERIES)
    suspend fun getDetailSeries(
        @Path("animeId") animeId: Int,
    ): Response<ApiResponse<List<DetailSeries>>>

    @GET(ApiConstants.GET_DETAIL_RECOMMENDATION)
    suspend fun getDetailRecommendation(
        @Path("animeId") animeId: Int,
    ): Response<ApiResponse<List<DefaultAnime>>>

    @GET(ApiConstants.GET_DETAIL_REVIEWS)
    suspend fun getDetailReviews(
        @Path("animeId") animeId: Int,
        @Query("sort") sort: String,
        @Query("isSpoiler") isSpoiler: Boolean,
        @Query("lastValue") lastValue: String?,
        @Query("lastId") lastId: Int?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<ReviewDetailResponse>>

    @POST(ApiConstants.SET_LIKE_ANIME)
    suspend fun setLikeAnime(
        @Path("animeId") animeId: Int,
    ): Response<ApiResponse<Unit>>

    @DELETE(ApiConstants.SET_LIKE_ANIME)
    suspend fun setUnLikeAnime(
        @Path("animeId") animeId: Int,
    ): Response<ApiResponse<Unit>>

    @POST(ApiConstants.SET_WATCH_STATUS)
    suspend fun createWatchStatus(
        @Path("animeId") animeId: Int,
        @Body request: WatchStatusRequest,
    ): Response<ApiResponse<Unit>>

    @PATCH(ApiConstants.SET_WATCH_STATUS)
    suspend fun updateWatchStatus(
        @Path("animeId") animeId: Int,
        @Body request: WatchStatusRequest,
    ): Response<ApiResponse<Unit>>

    @DELETE(ApiConstants.SET_WATCH_STATUS)
    suspend fun deleteWatchStatus(
        @Path("animeId") animeId: Int,
    ): Response<ApiResponse<Unit>>

    @GET(ApiConstants.GET_DETAIL_MY_REVIEW)
    suspend fun getMyReview(
        @Path("animeId") animeId: Int
    ): Response<ApiResponse<DetailMyReview>>

    @POST(ApiConstants.CREATE_ANIME_RATING)
    suspend fun createAnimeRating(
        @Path("animeId") animeId: Int,
        @Body request: ReviewRating
    ): Response<ApiResponse<Unit>>

    @PATCH(ApiConstants.UPDATE_ANIME_RATING)
    suspend fun updateAnimeRating(
        @Path("reviewId") reviewId: Int,
        @Body request: ReviewRating
    ): Response<ApiResponse<Unit>>

    @DELETE(ApiConstants.DELETE_ANIME_RATING)
    suspend fun deleteAnimeRating(
        @Path("reviewId") reviewId: Int
    ): Response<ApiResponse<Unit>>

    @GET(ApiConstants.GET_STUDIO_INFO)
    suspend fun getStudioInfo(
        @Path("studioId") studioId: Int,
        @Query("lastId") lastId: Int?,
        @Query("lastValue") lastValue: String?
    ): Response<ApiResponse<DetailStudio>>

    @GET(ApiConstants.GET_ANIME_ACTORS)
    suspend fun getAnimeActors(
        @Path("animeId") animeId: Int,
        @Query("lastId") lastId: Int?,
        @Query("lastValue") lastValue: String?
    ): Response<ApiResponse<AnimeActorsResponse>>

    @GET(ApiConstants.GET_ACTOR_INFO)
    suspend fun getActorInfo(
        @Path("personId") personId: Int,
        @Query("lastId") lastId: Int?,
    ): Response<ApiResponse<ActorDetailResponse>>

    @POST(ApiConstants.SET_LIKE_ACTOR)
    suspend fun likeActor(
        @Path("personId") personId: Int
    ): Response<ApiResponse<Unit>>

    @DELETE(ApiConstants.SET_LIKE_ACTOR)
    suspend fun unLikeActor(
        @Path("personId") personId: Int
    ): Response<ApiResponse<Unit>>
}