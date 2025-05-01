package jpark.bro.domain.usecase

import jpark.bro.domain.common.ActivityProvider
import jpark.bro.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithKakaoUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(activityProvider: ActivityProvider) {
        return authRepository.signInWithKakao(activityProvider)
    }
}