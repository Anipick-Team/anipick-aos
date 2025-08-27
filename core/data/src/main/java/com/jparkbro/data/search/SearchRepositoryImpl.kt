package com.jparkbro.data.search

import com.jparkbro.datastore.SearchDataStore
import com.jparkbro.model.search.SearchRequest
import com.jparkbro.model.search.SearchResponse
import com.jparkbro.model.search.SearchResultResponse
import com.jparkbro.model.search.SearchType
import com.jparkbro.network.search.SearchDataSource
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchDataSource: SearchDataSource,
    private val searchDataStore: SearchDataStore,
) : SearchRepository {

    override suspend fun saveSearchKeyword(keyword: String): Result<Unit> {
        return searchDataStore.saveSearchKeyword(keyword)
    }

    override suspend fun loadSearchKeyword(): Result<List<String>> {
        return searchDataStore.loadSearchKeyword()
    }

    override suspend fun deleteSearchKeyword(keyword: String): Result<Unit> {
        return searchDataStore.deleteSearchKeyword(keyword)
    }

    override suspend fun deleteAll(): Result<Unit> {
        return searchDataStore.deleteAll()
    }

    override suspend fun getPopularAnimes(): Result<SearchResponse> {
        return searchDataSource.getPopularAnimes()
    }

    override suspend fun getSearchResult(type: SearchType, request: SearchRequest): Result<SearchResultResponse> {
        return when (type) {
            SearchType.ANIMES -> searchDataSource.getSearchAnimes(request)
            SearchType.PERSONS -> searchDataSource.getSearchPersons(request)
            SearchType.STUDIOS -> searchDataSource.getSearchStudios(request)
        }
    }

    override suspend fun submitAnimeLog(logUrl: String): Result<Unit> {
        return searchDataSource.submitAnimeLog(logUrl)
    }
}