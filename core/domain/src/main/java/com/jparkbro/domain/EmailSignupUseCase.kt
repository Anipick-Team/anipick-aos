package com.jparkbro.domain

import com.jparkbro.data.AuthRepository
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.model.auth.SignupRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EmailSignupUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) {
    operator fun invoke(email: String, password: String, termsAndConditions: Boolean): Flow<Result<Boolean>> = flow {
        try {
            authRepository.emailSignup(
                SignupRequest(
                    email = email,
                    password = password,
                    termsAndConditions = termsAndConditions,
                )
            ).fold(
                onSuccess = { token ->
                    // 3. datastore 에 jwt token 저장
                    userPreferenceRepository.saveToken(token).fold(
                        onSuccess = {
                            // 4. 회원가입 이라 무조건 false 반환 > preferenceSetup 로 이동
                            emit(Result.success(false))
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