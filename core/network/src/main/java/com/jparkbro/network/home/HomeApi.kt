package com.jparkbro.network.home

import com.jparkbro.model.dto.home.main.NextQuarterAnimesResponse
import com.jparkbro.model.dto.home.main.RecommendedAnimesResponse
import com.jparkbro.model.common.anime.TrendingAnimeDto
import com.jparkbro.model.common.anime.UpcomingReleasesAnimeDto
import com.jparkbro.model.common.review.HomeReviewDto
import com.jparkbro.model.dto.home.detail.ListDataResponse
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {
    @GET(ApiConstants.TREND_ITEMS)
    suspend fun getTrendItems(
    ): Response<ApiResponse<List<TrendingAnimeDto>>>

    @GET(ApiConstants.RECOMMENDATION_ANIMES)
    suspend fun getRecommendItems(
    ): Response<ApiResponse<RecommendedAnimesResponse>>

    @GET(ApiConstants.RECOMMENDATION_ANIMES_RECENT)
    suspend fun getRecentRecommendItems(
        @Path("animeId") animeId: Int
    ): Response<ApiResponse<RecommendedAnimesResponse>>

    @GET(ApiConstants.REVIEW_RECENT)
    suspend fun getRecentReviews(
    ): Response<ApiResponse<List<HomeReviewDto>>>

    @GET(ApiConstants.UPCOMING_SEASON)
    suspend fun getNextQuarterAnimes(
    ): Response<ApiResponse<NextQuarterAnimesResponse>>

    @GET(ApiConstants.COMING_SOON)
    suspend fun getComingSoonItems(
    ): Response<ApiResponse<List<UpcomingReleasesAnimeDto>>>

    @GET(ApiConstants.HOME_DETAIL_RECOMMENDATION_ANIMES)
    suspend fun getDetailRecommends(
        @Query("lastId") lastId: Int?,
        @Query("lastValue") lastValue: String?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<ListDataResponse>>

    @GET(ApiConstants.HOME_DETAIL_RECOMMENDATION_ANIMES_RECENT)
    suspend fun getDetailRecentRecommends(
        @Path("animeId") animeId: Int,
        @Query("lastId") lastId: Int?,
        @Query("lastValue") lastValue: String?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<ListDataResponse>>

    @GET(ApiConstants.HOME_DETAIL_REVIEWS)
    suspend fun getDetailRecentReviews(
        @Query("lastId") lastId: Int?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<ListDataResponse>>

    @GET(ApiConstants.HOME_DETAIL_COMING_SOON)
    suspend fun getDetailComingSoon(
        @Query("sort") sort: String,
        @Query("lastId") lastId: Int?,
        @Query("lastValue") lastValue: String?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<ListDataResponse>>
}