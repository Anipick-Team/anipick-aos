package jpark.bro.anipick.domain.usecase

import jpark.bro.anipick.domain.repository.AuthRepository
import jpark.bro.anipick.domain.util.Result
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.signOut()
    }
}