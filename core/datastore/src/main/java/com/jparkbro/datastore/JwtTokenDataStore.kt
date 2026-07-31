package com.jparkbro.datastore

import com.jparkbro.model.auth.AuthToken

interface JwtTokenDataStore {
    suspend fun setToken(token: AuthToken? = null): Result<Unit>

    suspend fun getAccessToken(): Result<String?>           // interceptor 에서 사용,
    suspend fun getRefreshToken(): Result<String?>          // accessToken 만료시 새 토큰 서버에 요청,

}