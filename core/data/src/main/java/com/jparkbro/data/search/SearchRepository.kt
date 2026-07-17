package com.jparkbro.data.search

import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    /** (DataStore) Search Keyword */
    val searchKeywords: Flow<List<String>>
    suspend fun saveSearchKeyword(keyword: String): Result<Unit>
    suspend fun deleteSearchKeyword(keyword: String): Result<Unit>
    suspend fun deleteAll(): Result<Unit>
}