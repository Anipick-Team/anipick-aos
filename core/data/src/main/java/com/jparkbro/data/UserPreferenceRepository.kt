package com.jparkbro.data

import com.jparkbro.model.auth.AuthToken

interface UserPreferenceRepository {
    suspend fun saveToken(token: AuthToken): Result<Unit>                   // 서버에서 받아온 token 저장

    suspend fun saveUserInfo(userId: Long, nickname: String): Result<Unit>   // 로그인시 id, nickname 저장
}
