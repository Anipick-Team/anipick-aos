package com.jparkbro.data.search

import com.jparkbro.model.search.SearchRequest
import com.jparkbro.model.search.SearchResponse
import com.jparkbro.model.search.SearchResultResponse
import com.jparkbro.model.search.SearchType

interface SearchRepository {
    suspend fun saveSearchKeyword(keyword: String): Result<Unit>
    suspend fun loadSearchKeyword(): Result<List<String>>
    suspend fun deleteSearchKeyword(keyword: String): Result<Unit>
    suspend fun deleteAll(): Result<Unit>

    suspend fun getPopularAnimes(): Result<SearchResponse>      // Search API

    suspend fun getSearchResult(type: SearchType, request: SearchRequest): Result<SearchResultResponse>
}