package com.jparkbro.network.repository

import com.jparkbro.model.auth.AuthToken

interface TokenRepository {
    suspend fun saveToken(token: AuthToken): Result<Unit>
    suspend fun getAccessToken(): Result<String?>
    suspend fun getRefreshToken(): Result<String?>
}