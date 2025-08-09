package com.jparkbro.data

import com.jparkbro.model.auth.AuthToken

interface UserPreferenceRepository {
    suspend fun saveToken(token: AuthToken): Result<Unit>                   // 서버에서 받아온 token 저장
    suspend fun getAccessToken(): Result<String?>
    suspend fun getRefreshToken(): Result<String?>
    suspend fun requestToken(refreshToken: String): Result<AuthToken>

    suspend fun saveUserInfo(userId: Int, nickname: String): Result<Unit>   // 로그인시 id, nickname 저장
    suspend fun getUserId(): Result<Int>                                    // nickname 호출
    suspend fun getUserNickName(): Result<String>                           // nickname 호출

    suspend fun clearAllData(): Result<Unit>                                // 로그아웃 시 모든 데이터 삭제
}