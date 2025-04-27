package jpark.bro.anipick.domain.usecase

import android.app.Activity
import jpark.bro.anipick.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithKakaoUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(activity: Activity) {
        return authRepository.signInWithKakao(activity)
    }
}