package com.jparkbro.datastore

interface UserDataStore {
    suspend fun saveUserInfo(userId: Int, nickname: String): Result<Unit>
    suspend fun getUserId(): Result<Int>
    suspend fun getUserNickName(): Result<String>

    suspend fun clearUserInfo(): Result<Unit>
}