package com.jparkbro.domain

import android.util.Log
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.helper.decodeJWT
import com.jparkbro.helper.isTokenExpired
import com.jparkbro.model.common.Result
import com.jparkbro.model.common.asResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AutoLoginUserCase @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
) {
    operator fun invoke(): Flow<Result<Boolean>> = flow {
        // 1. datastore token 유무 확인
        val accessToken = userPreferenceRepository.getAccessToken().getOrNull()
        if (accessToken == null) {
            emit(false)
            return@flow
        } // 값 없으면 로그인 화면

        // 2. access token 만료 시간 확인 (디코딩)
        val isATExpired = isTokenExpired(accessToken)
        if (!isATExpired) {
            emit(true)
            return@flow
        } // 만료 안 됬으면 자동 로그인

        // 3. refresh token 만료 시간 확인 (디코딩)
        val refreshToken = userPreferenceRepository.getRefreshToken().getOrNull()
        if (refreshToken == null) {
            emit(false)
            return@flow
        }

        val isRTExpired = isTokenExpired(refreshToken)
        if (isRTExpired) {
            // 6. refresh token 만료 > 데이터 삭제
            userPreferenceRepository.clearAllData().getOrThrow()
            emit(false)
        } else {
            // 4. access token 만료 > 재요청
            val authToken = userPreferenceRepository.requestToken(refreshToken).getOrThrow()

            // 5. datastore token 저장
            userPreferenceRepository.saveToken(authToken).getOrThrow()
            emit(true)
        }
    }.asResult()
}