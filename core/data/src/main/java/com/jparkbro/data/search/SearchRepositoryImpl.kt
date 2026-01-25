package com.jparkbro.data.search

import com.jparkbro.datastore.SearchDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchDataStore: SearchDataStore,
) : SearchRepository {

    override val searchKeywords: Flow<List<String>> = searchDataStore.searchKeywords

    override suspend fun saveSearchKeyword(keyword: String): Result<Unit> {
        return searchDataStore.saveSearchKeyword(keyword)
    }

    override suspend fun deleteSearchKeyword(keyword: String): Result<Unit> {
        return searchDataStore.deleteSearchKeyword(keyword)
    }

    override suspend fun deleteAll(): Result<Unit> {
        return searchDataStore.deleteAll()
    }
}