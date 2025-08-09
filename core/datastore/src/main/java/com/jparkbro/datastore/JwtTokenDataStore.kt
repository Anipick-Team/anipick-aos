package com.jparkbro.datastore

import com.jparkbro.model.auth.AuthToken

interface JwtTokenDataStore {
    suspend fun saveToken(token: AuthToken): Result<Unit>   // 서버에서 받아온 token 저장
    suspend fun getAccessToken(): Result<String?>           // interceptor 에서 사용,
    suspend fun getRefreshToken(): Result<String?>          // accessToken 만료시 새 토큰 서버에 요청,

    suspend fun clearToken(): Result<Unit>                  // token 삭제
}