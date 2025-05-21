package jpark.bro.domain

import android.app.Activity
import jpark.bro.data.AuthRepository
import javax.inject.Inject

class GoogleLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(activity: Activity): Result<Boolean> {
        val tokenResult = authRepository.getGoogleAuthToken(activity)

        return tokenResult.fold(
            onSuccess = { socialToken ->
                val jwtResult = authRepository.socialLogin(socialToken)

                jwtResult.fold(
                    onSuccess = { jwt ->
                        try {
                            // TODO Datastore 저장 추가
                            Result.success(true)
                        } catch (e: Exception) {
                            Result.failure(e)
                        }
                    },
                    onFailure = {
                        Result.failure(it)
                    }
                )
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}