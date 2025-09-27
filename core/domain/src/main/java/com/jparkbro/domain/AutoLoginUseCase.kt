package com.jparkbro.domain

import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.helper.isTokenExpired
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AutoLoginUseCase @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository
) {
    operator fun invoke(): Flow<Result<Boolean>> = flow {
        try {
            // 1. datastore token 유무 확인
            userPreferenceRepository.getAccessToken().fold(
                onSuccess = { accessToken ->
                    if (accessToken == null) {
                        emit(Result.success(false))
                        return@flow
                    } // 값 없으면 로그인 화면

                    // 2. access token 만료 시간 확인 (디코딩)
                    val isATExpired = isTokenExpired(accessToken)
                    if (!isATExpired) {
                        emit(Result.success(true))
                        return@flow
                    } // 만료 안 됬으면 자동 로그인

                    // 3. refresh token 만료 시간 확인 (디코딩)
                    userPreferenceRepository.getRefreshToken().fold(
                        onSuccess = { refreshToken ->
                            if (refreshToken == null) {
                                emit(Result.success(false))
                                return@flow
                            }

                            val isRTExpired = isTokenExpired(refreshToken)
                            if (isRTExpired) {
                                // 6. refresh token 만료 > 데이터 삭제
                                userPreferenceRepository.clearAllData().fold(
                                    onSuccess = {
                                        emit(Result.success(false))
                                    },
                                    onFailure = { exception ->
                                        emit(Result.failure(exception))
                                    }
                                )
                            } else {
                                // 4. access token 만료 > 재요청
                                userPreferenceRepository.requestToken(refreshToken).fold(
                                    onSuccess = { authToken ->
                                        // 5. datastore token 저장
                                        userPreferenceRepository.saveToken(authToken).fold(
                                            onSuccess = {
                                                emit(Result.success(true))
                                            },
                                            onFailure = { exception ->
                                                emit(Result.failure(exception))
                                            }
                                        )
                                    },
                                    onFailure = { exception ->
                                        emit(Result.success(false)) // token 재발급 실패는 로그인 화면으로
                                    }
                                )
                            }
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