package jpark.bro.anipick.domain.usecase

import jpark.bro.anipick.domain.model.User
import jpark.bro.anipick.domain.util.Result
import jpark.bro.anipick.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<User> {
        return authRepository.signInWithGoogle()
    }
}