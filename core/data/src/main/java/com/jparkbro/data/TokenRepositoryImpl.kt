package com.jparkbro.data

import com.jparkbro.datastore.JwtTokenDataStore
import com.jparkbro.model.auth.AuthToken
import com.jparkbro.network.auth.AuthDataSource
import com.jparkbro.network.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val dataStore: JwtTokenDataStore
) : TokenRepository {
    override suspend fun saveToken(token: AuthToken): Result<Unit> {
        return dataStore.saveToken(token)
    }

    override suspend fun getAccessToken(): Result<String?> {
        return dataStore.getAccessToken()
    }

    override suspend fun getRefreshToken(): Result<String?> {
        return dataStore.getRefreshToken()
    }
}