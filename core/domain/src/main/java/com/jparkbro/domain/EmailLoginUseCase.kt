package com.jparkbro.domain

import com.jparkbro.data.AuthRepository
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.model.auth.EmailLoginRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EmailLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) {
    operator fun invoke(email: String, password: String): Flow<Result<Boolean>> = flow {
        try {
            authRepository.emailLogin(EmailLoginRequest(email = email, password = password)).fold(
                onSuccess = { response ->
                    // datastore 에 jwt token 저장
                    userPreferenceRepository.saveToken(response.token).fold(
                        onSuccess = {
                            // datastore 에 id, nickname 저장
                            userPreferenceRepository.saveUserInfo(response.userId, response.nickname).fold(
                                onSuccess = {
                                    // 5. 모두 성공시 페이지 분기 값 반환
                                    emit(Result.success(response.reviewCompletedYn))
                                },
                                onFailure = { exception ->
                                    emit(Result.failure(exception))
                                }
                            )
                        },
                        onFailure = { exception ->
                            emit(Result.failure(exception))
                        }
                    )
                },
                onFailure = { exception ->
                    emit(Result.failure(exception))
                }
            )
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}