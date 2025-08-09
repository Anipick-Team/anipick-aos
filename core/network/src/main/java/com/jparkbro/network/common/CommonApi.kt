package com.jparkbro.network.common

import com.jparkbro.model.auth.AuthToken
import com.jparkbro.model.common.MetaData
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CommonApi {
    @GET(ApiConstants.GET_META_DATA)
    suspend fun getMetaData(
    ): Response<ApiResponse<MetaData>>

    @POST(ApiConstants.TOKEN_REFRESH)
    suspend fun requestToken(
        @Header("Authorization") refreshToken: String
    ): Response<ApiResponse<AuthToken>>
}