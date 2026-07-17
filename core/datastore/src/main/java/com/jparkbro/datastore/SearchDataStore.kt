package com.jparkbro.datastore

import kotlinx.coroutines.flow.Flow

interface SearchDataStore {
    val searchKeywords: Flow<List<String>>
    suspend fun saveSearchKeyword(keyWord: String): Result<Unit>
    suspend fun deleteSearchKeyword(keyWord: String): Result<Unit>
    suspend fun deleteAll(): Result<Unit>
}