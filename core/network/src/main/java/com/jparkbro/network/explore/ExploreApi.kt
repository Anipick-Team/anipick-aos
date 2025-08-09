package com.jparkbro.network.explore

import com.jparkbro.model.explore.ExploreResponse
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExploreApi {
    @GET(ApiConstants.EXPLORE_ANIMES)
    suspend fun exploreAnime(
        @Query("year") year: String?,
        @Query("season") season: Int?,
        @Query("genres") genres: List<Int>?,
        @Query("type") type: String?,
        @Query("sort") sort: String,
        @Query("lastId") lastId: Int?,
        @Query("size") size: Int,
        @Query("genreOp") genreOp: String,
        @Query("lastValue") lastValue: String?,
    ): Response<ApiResponse<ExploreResponse>>
}