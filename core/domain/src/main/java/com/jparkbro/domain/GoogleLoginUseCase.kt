package com.jparkbro.domain

import android.app.Activity
import com.jparkbro.data.AuthRepository
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.model.auth.LoginProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GoogleLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) {
    operator fun invoke(activity: Activity): Flow<Result<Boolean>> = flow {
        try {
            // 1. social api token 발급
            authRepository.getGoogleAuthToken(activity).fold(
                onSuccess = { socialToken ->
                    // 2. 백엔드 api 통신
                    authRepository.socialLogin(
                        provider = LoginProvider.GOOGLE,
                        socialToken = socialToken,
                    ).fold(
                        onSuccess = { response ->
                            // 3. datastore 에 jwt token 저장
                            userPreferenceRepository.saveToken(response.token).fold(
                                onSuccess = {
                                    // 4. datastore 에 id, nickname 저장
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