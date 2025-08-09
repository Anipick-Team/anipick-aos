package com.jparkbro.network.auth

import com.jparkbro.model.auth.AuthResponse
import com.jparkbro.model.auth.AuthToken
import com.jparkbro.model.auth.EmailLoginRequest
import com.jparkbro.model.auth.PreferenceRequest
import com.jparkbro.model.auth.PreferenceResponse
import com.jparkbro.model.auth.RatedAnime
import com.jparkbro.model.auth.RequestCode
import com.jparkbro.model.auth.ResetPassword
import com.jparkbro.model.auth.SignupRequest
import com.jparkbro.model.auth.LoginProvider
import com.jparkbro.model.auth.VerifyCode

/**
 * Interface representing network calls to the AniPick backend
 */
interface AuthDataSource {
    suspend fun socialLogin(
        provider: LoginProvider,
        socialToken: String,
    ): Result<AuthResponse>

    suspend fun emailSignup(
        request: SignupRequest
    ): Result<AuthToken>

    suspend fun emailLogin(
        request: EmailLoginRequest
    ): Result<AuthResponse>

    suspend fun requestResetCode(
        request: RequestCode
    ): Result<Unit>

    suspend fun verifyResetCode(
        request: VerifyCode
    ): Result<Unit>

    suspend fun resetPassword(
        request: ResetPassword
    ): Result<Unit>

    suspend fun exploreOrSearch(
        request: PreferenceRequest
    ): Result<PreferenceResponse>

    suspend fun submitReviews(
        request: List<RatedAnime>
    ): Result<Unit>
}