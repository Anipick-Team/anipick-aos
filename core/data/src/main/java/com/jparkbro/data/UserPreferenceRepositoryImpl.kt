package com.jparkbro.data

import com.jparkbro.datastore.JwtTokenDataStore
import com.jparkbro.datastore.UserDataStore
import com.jparkbro.model.auth.AuthToken

class UserPreferenceRepositoryImpl(
    private val jwtTokenDataStore: JwtTokenDataStore,
    private val userDataStore: UserDataStore,
) : UserPreferenceRepository {
    override suspend fun saveToken(token: AuthToken): Result<Unit> {
        return jwtTokenDataStore.setToken(token)
    }

    override suspend fun saveUserInfo(userId: Long, nickname: String): Result<Unit> {
        return userDataStore.saveUserInfo(userId, nickname)
    }
}
