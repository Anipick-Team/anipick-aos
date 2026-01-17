package com.jparkbro.network.anime

import com.jparkbro.model.common.anime.InfoSeriesAnimeDto
import com.jparkbro.model.common.anime.SimpleAnimeDto
import com.jparkbro.model.detail.AnimeRecommendsResponse
import com.jparkbro.model.detail.AnimeSeriesResponse
import com.jparkbro.model.detail.WatchStatusRequest
import com.jparkbro.model.dto.home.main.RecommendedAnimesResponse
import com.jparkbro.model.dto.info.AnimeInfoResponse
import com.jparkbro.model.dto.info.GetInfoRecommendResponse
import com.jparkbro.model.dto.info.GetInfoSeriesResponse
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResponse
import com.jparkbro.model.mypage.UserContentResponse
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

interface AnimeApi {
    @GET(ApiConstants.RECOMMENDATION_ANIMES_RECENT)
    suspend fun getRecentRecommendItems(
        @Path("animeId") animeId: Long
    ): Response<ApiResponse<RecommendedAnimesResponse>>

    @GET(ApiConstants.GET_DETAIL_INFO)
    suspend fun getDetailInfo(
        @Path("animeId") animeId: Long,
    ): Response<ApiResponse<AnimeInfoResponse>>

    @GET(ApiConstants.GET_DETAIL_SERIES)
    suspend fun getDetailSeries(
        @Path("animeId") animeId: Long,
    ): Response<ApiResponse<List<InfoSeriesAnimeDto>>>

    @GET(ApiConstants.GET_DETAIL_RECOMMENDATION)
    suspend fun getDetailRecommendation(
        @Path("animeId") animeId: Long,
    ): Response<ApiResponse<List<SimpleAnimeDto>>>

    @POST(ApiConstants.SET_LIKE_ANIME)
    suspend fun setLikeAnime(
        @Path("animeId") animeId: Long,
    ): Response<ApiResponse<Unit>>

    @DELETE(ApiConstants.SET_LIKE_ANIME)
    suspend fun setUnlikeAnime(
        @Path("animeId") animeId: Long,
    ): Response<ApiResponse<Unit>>

    @POST(ApiConstants.SET_WATCH_STATUS)
    suspend fun createWatchStatus(
        @Path("animeId") animeId: Long,
        @Body request: WatchStatusRequest,
    ): Response<ApiResponse<Unit>>

    @PATCH(ApiConstants.SET_WATCH_STATUS)
    suspend fun updateWatchStatus(
        @Path("animeId") animeId: Long,
        @Body request: WatchStatusRequest,
    ): Response<ApiResponse<Unit>>

    @DELETE(ApiConstants.SET_WATCH_STATUS)
    suspend fun deleteWatchStatus(
        @Path("animeId") animeId: Long,
    ): Response<ApiResponse<Unit>>

    @GET(ApiConstants.GET_ANIME_SERIES)
    suspend fun getAnimeSeries(
        @Path("animeId") animeId: Long,
        @Query("lastId") lastId: Long?
    ): Response<ApiResponse<GetInfoSeriesResponse>>

    @GET(ApiConstants.GET_ANIME_RECOMMENDS)
    suspend fun getAnimeRecommends(
        @Path("animeId") animeId: Long,
        @Query("lastId") lastId: Long?,
    ): Response<ApiResponse<GetInfoRecommendResponse>>

    @GET(ApiConstants.WATCH_LIST)
    suspend fun loadWatchListAnimes(
        @Query("status") status: String,
        @Query("lastId") lastId: Long?,
    ): Response<ApiResponse<GetUserContentResponse>>

    @GET(ApiConstants.WATCHING)
    suspend fun loadWatchingAnimes(
        @Query("status") status: String,
        @Query("lastId") lastId: Long?,
    ): Response<ApiResponse<GetUserContentResponse>>

    @GET(ApiConstants.FINISHED)
    suspend fun loadFinishedAnimes(
        @Query("status") status: String,
        @Query("lastId") lastId: Long?,
    ): Response<ApiResponse<GetUserContentResponse>>

    @GET(ApiConstants.LIKE_ANIMES)
    suspend fun loadLikedAnimes(
        @Query("lastId") lastId: Long?,
    ): Response<ApiResponse<GetUserContentResponse>>
}