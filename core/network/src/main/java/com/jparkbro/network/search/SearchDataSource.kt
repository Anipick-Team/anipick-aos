package com.jparkbro.network.search

import com.jparkbro.model.search.SearchRequest
import com.jparkbro.model.search.SearchResponse
import com.jparkbro.model.search.SearchResultResponse

interface SearchDataSource {
    suspend fun getPopularAnimes(): Result<SearchResponse>

    suspend fun getSearchAnimes(request: SearchRequest): Result<SearchResultResponse>
    suspend fun getSearchPersons(request: SearchRequest): Result<SearchResultResponse>
    suspend fun getSearchStudios(request: SearchRequest): Result<SearchResultResponse>

    suspend fun submitAnimeLog(logUrl: String): Result<Unit>
}