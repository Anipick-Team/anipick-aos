package com.jparkbro.network.actor

import com.jparkbro.model.dto.info.InfoActorResponse
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ActorApi {
    @GET(ApiConstants.GET_DETAIL_ACTOR)
    suspend fun getDetailActor(
        @Path("animeId") animeId: Int,
    ): Response<ApiResponse<List<InfoActorResponse>>>
}