package com.jparkbro.network.ranking

import com.jparkbro.model.ranking.RankingResponse
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RankingApi {
    @GET(ApiConstants.RANK_REAL_TIME)
    suspend fun getRealTimeRanking(
        @Query("genre") genre: String?,
        @Query("lastId") lastId: Int?,
        @Query("lastValue") lastValue: Int?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<RankingResponse>>

    @GET(ApiConstants.RANK_YEAR_SEASON)
    suspend fun getYearSeasonRanking(
        @Query("year") year: String?,
        @Query("season") season: Int?,
        @Query("genre") genre: String?,
        @Query("lastId") lastId: Int?,
        @Query("lastRank") lastRank: Int?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<RankingResponse>>

    @GET(ApiConstants.RANK_ALL_TIME)
    suspend fun getAllTimeRanking(
        @Query("genre") genre: String?,
        @Query("lastId") lastId: Int?,
        @Query("lastRank") lastRank: Int?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<RankingResponse>>
}