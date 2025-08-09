package com.jparkbro.domain

import android.util.Log
import com.jparkbro.data.AuthRepository
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.model.auth.EmailLoginRequest
import com.jparkbro.model.common.Result
import com.jparkbro.model.common.asResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EmailLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) {
    operator fun invoke(email: String, password: String): Flow<Result<Boolean>> = flow {
        val response = authRepository.emailLogin(EmailLoginRequest(email = email, password = password,)).getOrThrow()

        // datastore 에 jwt token 저장
        userPreferenceRepository.saveToken(response.token).getOrThrow()
        // datastore 에 id, nickname 저장
        userPreferenceRepository.saveUserInfo(response.userId, response.nickname).getOrThrow()

        // 5. 모두 성공시 페이지 분기 값 반환
        emit(response.reviewCompletedYn)
    }.asResult()
}