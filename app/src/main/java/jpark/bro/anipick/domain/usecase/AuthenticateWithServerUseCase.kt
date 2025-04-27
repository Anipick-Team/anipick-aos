package jpark.bro.anipick.domain.usecase

import jpark.bro.anipick.domain.repository.AuthRepository
import javax.inject.Inject

class AuthenticateWithServerUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
//    suspend operator fun invoke(idToken: String): Result<Unit> {
//        return authRepository.authenticateWithServer(idToken)
//    }
}