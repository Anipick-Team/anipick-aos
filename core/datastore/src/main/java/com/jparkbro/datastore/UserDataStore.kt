package com.jparkbro.datastore

interface UserDataStore {
    suspend fun saveUserInfo(userId: Long, nickname: String): Result<Unit>
    suspend fun getUserId(): Result<Long>
    suspend fun getUserNickName(): Result<String>

    suspend fun clearUserInfo(): Result<Unit>
}