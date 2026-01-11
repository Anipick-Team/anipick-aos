package com.jparkbro.network.studio

import com.jparkbro.model.dto.studio.GetStudioInfoResponse
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StudioApi {
    @GET(ApiConstants.GET_STUDIO_INFO)
    suspend fun getStudioInfo(
        @Path("studioId") studioId: Long,
        @Query("lastId") lastId: Long?,
        @Query("lastValue") lastValue: String?
    ): Response<ApiResponse<GetStudioInfoResponse>>
}