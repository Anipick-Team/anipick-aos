package com.jparkbro.network.search

import com.jparkbro.model.search.SearchResponse
import com.jparkbro.model.search.SearchResultResponse
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.retrofit.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface SearchApi {
    @GET(ApiConstants.GET_POPULAR_ANIMES)
    suspend fun getPopularAnimes(
    ): Response<ApiResponse<SearchResponse>>

    @GET(ApiConstants.GET_SEARCH_ANIMES)
    suspend fun getSearchAnimes(
        @Query("query") query: String,
        @Query("lastId") lastId: Int?,
        @Query("size") size: Int?,
        @Query("page") page: Int?,
    ): Response<ApiResponse<SearchResultResponse>>

    @GET(ApiConstants.GET_SEARCH_PERSONS)
    suspend fun getSearchPersons(
        @Query("query") query: String,
        @Query("lastId") lastId: Int?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<SearchResultResponse>>

    @GET(ApiConstants.GET_SEARCH_STUDIOS)
    suspend fun getSearchStudios(
        @Query("query") query: String,
        @Query("lastId") lastId: Int?,
        @Query("size") size: Int?,
    ): Response<ApiResponse<SearchResultResponse>>

    @GET
    suspend fun submitLogByUrl(
        @Url url: String
    ): Response<ApiResponse<Unit>>
}