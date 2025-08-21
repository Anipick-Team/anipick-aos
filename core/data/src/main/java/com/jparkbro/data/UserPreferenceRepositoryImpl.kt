package com.jparkbro.data

import com.jparkbro.datastore.JwtTokenDataStore
import com.jparkbro.datastore.UserDataStore
import com.jparkbro.model.auth.AuthToken
import com.jparkbro.network.common.CommonDataSource
import javax.inject.Inject

class UserPreferenceRepositoryImpl @Inject constructor(
    private val jwtTokenDataStore: JwtTokenDataStore,
    private val commonDataSource: CommonDataSource,
    private val userDataStore: UserDataStore,
) : UserPreferenceRepository {
    override suspend fun saveToken(token: AuthToken): Result<Unit> {
        return jwtTokenDataStore.saveToken(token)
    }

    override suspend fun getAccessToken(): Result<String?> {
        return jwtTokenDataStore.getAccessToken()
    }

    override suspend fun getRefreshToken(): Result<String?> {
        return jwtTokenDataStore.getRefreshToken()
    }

    override suspend fun requestToken(refreshToken: String): Result<AuthToken> {
        return commonDataSource.requestToken(refreshToken)
    }

    override suspend fun saveUserInfo(userId: Int, nickname: String): Result<Unit> {
        return userDataStore.saveUserInfo(userId, nickname)
    }

    override suspend fun getUserId(): Result<Int> {
        return userDataStore.getUserId()
    }

    override suspend fun getUserNickName(): Result<String> {
        return userDataStore.getUserNickName()
    }

    override suspend fun clearAllData(): Result<Unit> {
        return runCatching {
            jwtTokenDataStore.clearToken().getOrThrow()
            userDataStore.clearUserInfo().getOrThrow()
        }
    }
}