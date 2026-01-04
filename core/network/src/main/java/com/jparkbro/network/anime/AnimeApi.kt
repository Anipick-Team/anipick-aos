package com.jparkbro.network.anime

import com.jparkbro.model.common.anime.InfoSeriesAnimeDto
import com.jparkbro.model.common.anime.SimpleAnimeDto
import com.jparkbro.model.dto.info.AnimeInfoResponse
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimeApi {
    @GET(ApiConstants.GET_DETAIL_INFO)
    suspend fun getDetailInfo(
        @Path("animeId") animeId: Int,
    ): Response<ApiResponse<AnimeInfoResponse>>

    @GET(ApiConstants.GET_DETAIL_SERIES)
    suspend fun getDetailSeries(
        @Path("animeId") animeId: Int,
    ): Response<ApiResponse<List<InfoSeriesAnimeDto>>>

    @GET(ApiConstants.GET_DETAIL_RECOMMENDATION)
    suspend fun getDetailRecommendation(
        @Path("animeId") animeId: Int,
    ): Response<ApiResponse<List<SimpleAnimeDto>>>
}