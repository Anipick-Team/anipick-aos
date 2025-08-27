package com.jparkbro.network.search

import com.jparkbro.model.search.SearchRequest
import com.jparkbro.model.search.SearchResponse
import com.jparkbro.model.search.SearchResultResponse
import com.jparkbro.network.util.toResult
import com.jparkbro.network.util.toUnitResult
import javax.inject.Inject

class RetrofitSearchDataSource @Inject constructor(
    private val searchApi: SearchApi
) : SearchDataSource {
    companion object {
        private const val TAG = "RetrofitSearchDataSource"
    }

    override suspend fun getPopularAnimes(): Result<SearchResponse> {
        return searchApi.getPopularAnimes().toResult(TAG, "getPopularAnimes")
    }

    override suspend fun getSearchAnimes(request: SearchRequest): Result<SearchResultResponse> {
        return searchApi.getSearchAnimes(
            query = request.query,
            lastId = request.lastId,
            size = request.size,
            page = request.page
        ).toResult(TAG, "getSearchAnimes")
    }

    override suspend fun getSearchPersons(request: SearchRequest): Result<SearchResultResponse> {
        return searchApi.getSearchPersons(
            query = request.query,
            lastId = request.lastId,
            size = request.size,
        ).toResult(TAG, "getSearchPersons")
    }

    override suspend fun getSearchStudios(request: SearchRequest): Result<SearchResultResponse> {
        return searchApi.getSearchStudios(
            query = request.query,
            lastId = request.lastId,
            size = request.size,
        ).toResult(TAG, "getSearchStudios")
    }

    override suspend fun submitAnimeLog(logUrl: String): Result<Unit> {
        return searchApi.submitLogByUrl(
            url = logUrl
        ).toUnitResult(TAG, "submitAnimeLog")
    }
}