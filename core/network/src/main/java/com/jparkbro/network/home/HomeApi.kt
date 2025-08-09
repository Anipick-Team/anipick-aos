package com.jparkbro.network.home

import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.home.ComingSoonItem
import com.jparkbro.model.home.HomeDetailResponse
import com.jparkbro.model.home.HomeRecommendResponse
import com.jparkbro.model.home.HomeReviewItem
import com.jparkbro.model.home.UpcomingSeasonItems
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {
    @GET(ApiConstants.TREND_ITEMS)
    suspend fun getTrendItems(
    ): Response<ApiResponse<List<DefaultAnime>>>

    @GET(ApiConstants.RECOMMENDATION_ANIMES)
    suspend fun getRecommendItems(
    ): Response<ApiResponse<HomeRecommendResponse>>

    @GET(ApiConstants.RECOMMENDATION_ANIMES_RECENT)
    suspend fun getRecentRecommendItems(
        @Path("animeId") animeId: Int
    ): Response<ApiResponse<HomeRecommendResponse>>

    @GET(ApiConstants.REVIEW_RECENT)
    suspend fun getRecentReviews(
    ): Response<ApiResponse<List<HomeReviewItem>>>

    @GET(ApiConstants.UPCOMING_SEASON)
    suspend fun getUpcomingSeasonItems(
    ): Response<ApiResponse<UpcomingSeasonItems>>

    @GET(ApiConstants.COMING_SOON)
    suspend fun getComingSoonItems(
    ): Response<ApiResponse<List<ComingSoonItem>>>

    @GET(ApiConstants.HOME_DETAIL_RECOMMENDATION_ANIMES)
    suspend fun getDetailRecommends(
        @Query("lastId") lastId: Int?,
        @Query("lastValue") lastValue: String?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<HomeDetailResponse>>

    @GET(ApiConstants.HOME_DETAIL_RECOMMENDATION_ANIMES_RECENT)
    suspend fun getDetailRecentRecommends(
        @Path("animeId") animeId: Int,
        @Query("lastId") lastId: Int?,
        @Query("lastValue") lastValue: String?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<HomeDetailResponse>>

    @GET(ApiConstants.HOME_DETAIL_REVIEWS)
    suspend fun getDetailRecentReviews(
        @Query("lastId") lastId: Int?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<HomeDetailResponse>>

    @GET(ApiConstants.HOME_DETAIL_COMING_SOON)
    suspend fun getDetailComingSoon(
        @Query("sort") sort: String,
        @Query("lastId") lastId: Int?,
        @Query("lastValue") lastValue: String?,
        @Query("includeAdult") includeAdult: Boolean,
        @Query("size") size: Int?,
    ): Response<ApiResponse<HomeDetailResponse>>
}