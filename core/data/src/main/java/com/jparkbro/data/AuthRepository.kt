package com.jparkbro.data

import android.app.Activity
import com.jparkbro.model.auth.AuthToken
import com.jparkbro.model.auth.LoginProvider
import com.jparkbro.model.auth.AuthResponse
import com.jparkbro.model.auth.EmailLoginRequest
import com.jparkbro.model.auth.PreferenceRequest
import com.jparkbro.model.auth.PreferenceResponse
import com.jparkbro.model.auth.RatedAnime
import com.jparkbro.model.auth.RequestCode
import com.jparkbro.model.auth.ResetPassword
import com.jparkbro.model.auth.SignupRequest
import com.jparkbro.model.auth.VerifyCode

interface AuthRepository {

    suspend fun emailSignup(request: SignupRequest): Result<AuthToken>
    suspend fun emailLogin(request: EmailLoginRequest): Result<AuthResponse>
    suspend fun getKakaoAuthToken(activity: Activity): Result<String>
    suspend fun getGoogleAuthToken(activity: Activity): Result<String>
    suspend fun socialLogin(provider: LoginProvider, socialToken: String): Result<AuthResponse>

    /* 비밀번호 찾기 로직 */
    suspend fun requestResetCode(request: RequestCode): Result<Unit>
    suspend fun verifyResetCode(request: VerifyCode): Result<Unit>
    suspend fun resetPassword(request: ResetPassword): Result<Unit>

    /* 애니평가 */
    suspend fun exploreOrSearch(request: PreferenceRequest): Result<PreferenceResponse>
    suspend fun submitReviews(request: List<RatedAnime>): Result<Unit>
}