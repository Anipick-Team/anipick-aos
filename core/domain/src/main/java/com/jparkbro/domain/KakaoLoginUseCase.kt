package com.jparkbro.domain

import android.app.Activity
import com.jparkbro.data.AuthRepository
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.model.auth.LoginProvider
import com.jparkbro.model.common.Result
import com.jparkbro.model.common.asResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KakaoLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) {
    operator fun invoke(activity: Activity): Flow<Result<Boolean>> = flow {
        // 1. social api token 발급
        val socialToken = authRepository.getKakaoAuthToken(activity).getOrThrow()

        // 2. 백엔드 api 통신
        authRepository.socialLogin(
            provider = LoginProvider.KAKAO,
            socialToken = socialToken
        ).fold(
            onSuccess = { response ->
                // 3. datastore 에 jwt token 저장
                userPreferenceRepository.saveToken(response.token).getOrThrow()
                // 4. datastore 에 id, nickname 저장
                userPreferenceRepository.saveUserInfo(response.userId, response.nickname).getOrThrow()

                // 5. 모두 성공시 페이지 분기 값 반환
                emit(response.reviewCompletedYn)
            },
            onFailure = {
                // TODO error
            }
        )
    }.asResult()
}