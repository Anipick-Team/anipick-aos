package jpark.bro.domain.usecase

import jpark.bro.domain.repository.AuthRepository
import javax.inject.Inject

class CheckAuthStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
//    operator fun invoke(): Flow<Boolean> {
//        return authRepository.isAuthenticated
//    }
}