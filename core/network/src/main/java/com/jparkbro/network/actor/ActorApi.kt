package com.jparkbro.network.actor

import com.jparkbro.model.dto.actor.GetActorResponse
import com.jparkbro.model.dto.info.GetInfoCharactersResponse
import com.jparkbro.model.dto.info.InfoActorResponse
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResponse
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResult
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ActorApi {
    @GET(ApiConstants.GET_DETAIL_ACTOR)
    suspend fun getDetailActor(
        @Path("animeId") animeId: Long,
    ): Response<ApiResponse<List<InfoActorResponse>>>

    @GET(ApiConstants.GET_ANIME_ACTORS)
    suspend fun getInfoCharacters(
        @Path("animeId") animeId: Long,
        @Query("lastId") lastId: Long?,
        @Query("lastValue") lastValue: String?
    ): Response<ApiResponse<GetInfoCharactersResponse>>

    @GET(ApiConstants.GET_ACTOR_INFO)
    suspend fun getActor(
        @Path("personId") personId: Long,
        @Query("lastId") lastId: Long?,
    ): Response<ApiResponse<GetActorResponse>>

    @POST(ApiConstants.SET_LIKE_ACTOR)
    suspend fun likeActor(
        @Path("personId") personId: Long
    ): Response<ApiResponse<Unit>>

    @DELETE(ApiConstants.SET_LIKE_ACTOR)
    suspend fun unLikeActor(
        @Path("personId") personId: Long
    ): Response<ApiResponse<Unit>>

    @GET(ApiConstants.LIKE_PERSONS)
    suspend fun loadUserContentActors(
        @Query("lastId") lastId: Long?,
    ): Response<ApiResponse<GetUserContentResponse>>
}