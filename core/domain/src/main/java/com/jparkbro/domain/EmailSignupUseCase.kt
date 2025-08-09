package com.jparkbro.domain

import com.jparkbro.data.AuthRepository
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.model.auth.SignupRequest
import com.jparkbro.model.common.Result
import com.jparkbro.model.common.asResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EmailSignupUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) {
    operator fun invoke(email: String, password: String, termsAndConditions: Boolean): Flow<Result<Boolean>> = flow {
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
                        emit(false)
                    },
                    onFailure = {
                        // TODO error
                    }
                )
            },
            onFailure = {
                // TODO 에러 처리
            }
        )
    }.asResult()
}