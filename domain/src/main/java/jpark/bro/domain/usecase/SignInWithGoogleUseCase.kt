package jpark.bro.domain.usecase

import jpark.bro.domain.model.User
import jpark.bro.domain.util.ApiResult
import jpark.bro.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): ApiResult<User> {
        return authRepository.signInWithGoogle()
    }
}