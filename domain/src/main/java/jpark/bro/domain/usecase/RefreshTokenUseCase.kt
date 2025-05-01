package jpark.bro.domain.usecase

import jpark.bro.domain.repository.AuthRepository
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
//    suspend operator fun invoke(): Result<Unit> {
//        return authRepository.refreshToken()
//    }
}