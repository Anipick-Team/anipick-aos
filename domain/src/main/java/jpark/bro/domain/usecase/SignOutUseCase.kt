package jpark.bro.domain.usecase

import jpark.bro.domain.repository.AuthRepository
import jpark.bro.domain.util.ApiResult
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): ApiResult<Unit> {
        return authRepository.signOut()
    }
}