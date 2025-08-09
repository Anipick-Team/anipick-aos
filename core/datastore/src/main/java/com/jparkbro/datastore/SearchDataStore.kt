package com.jparkbro.datastore

interface SearchDataStore {
    suspend fun saveSearchKeyword(keyWord: String): Result<Unit>
    suspend fun loadSearchKeyword(): Result<List<String>>
    suspend fun deleteSearchKeyword(keyWord: String): Result<Unit>
    suspend fun deleteAll(): Result<Unit>
}