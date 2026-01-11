package com.jparkbro.domain

import com.jparkbro.data.AuthRepository
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.model.dto.auth.EmailRegisterRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EmailRegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) {
    operator fun invoke(email: String, password: String, termsAndConditions: Boolean): Flow<Result<Boolean>> = flow {
        try {
            authRepository.emailSignup(
                EmailRegisterRequest(
                    email = email,
                    password = password,
                    termsAndConditions = termsAndConditions,
                )
            ).fold(
                onSuccess = { response ->
                    // 3. datastore 에 jwt token 저장
                    userPreferenceRepository.saveToken(response.token).fold(
                        onSuccess = {
                            userPreferenceRepository.saveUserInfo(response.userId, response.nickname).fold(
                                onSuccess = {
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