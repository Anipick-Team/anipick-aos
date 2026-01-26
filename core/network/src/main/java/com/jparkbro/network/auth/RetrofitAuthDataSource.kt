package com.jparkbro.network.auth

import com.jparkbro.model.auth.AuthResponse
import com.jparkbro.model.auth.EmailLoginRequest
import com.jparkbro.model.auth.LoginProvider
import com.jparkbro.model.auth.RequestCode
import com.jparkbro.model.auth.ResetPassword
import com.jparkbro.model.auth.SocialLoginRequest
import com.jparkbro.model.auth.VerifyCode
import com.jparkbro.model.dto.auth.EmailRegisterRequest
import com.jparkbro.model.dto.auth.EmailRegisterResponse
import com.jparkbro.model.dto.preference.RatedAnime
import com.jparkbro.model.dto.preference.SearchRequest
import com.jparkbro.model.dto.preference.SearchResponse
import com.jparkbro.network.util.toResult
import com.jparkbro.network.util.toUnitResult
import javax.inject.Inject

/**
 * network data source 구현체
 */
internal class RetrofitAuthDataSource @Inject constructor(
    private val authApi: AuthApi
) : AuthDataSource {
    companion object {
        private const val TAG = "RetrofitAuthDataSource"
    }

    override suspend fun socialLogin(provider: LoginProvider, socialToken: String): Result<AuthResponse> {
       return authApi.socialLogin(
           provider = provider.value,
           request = SocialLoginRequest(
               platform = "android",
               code = socialToken
           )
       ).toResult(TAG, "socialLogin")
    }

    override suspend fun emailSignup(request: EmailRegisterRequest): Result<EmailRegisterResponse> {
        return authApi.emailSignup(request).toResult(TAG, "emailSignup")
    }


    override suspend fun emailLogin(request: EmailLoginRequest): Result<AuthResponse> {
        return authApi.emailLogin(request).toResult(TAG, "emailLogin")
    }

    override suspend fun requestResetCode(request: RequestCode): Result<Unit> {
        return authApi.requestResetCode(request).toUnitResult(TAG, "requestResetCode")
    }

    override suspend fun verifyResetCode(request: VerifyCode): Result<Unit> {
        return authApi.verifyResetCode(request).toUnitResult(TAG, "verifyResetCode")
    }

    override suspend fun resetPassword(request: ResetPassword): Result<Unit> {
        return authApi.resetPassword(request).toUnitResult(TAG, "resetPassword")
    }

    override suspend fun exploreOrSearch(request: SearchRequest): Result<SearchResponse> {
        return authApi.exploreOrSearch(
            query = request.query,
            year = request.year,
            season = request.season,
            genres = request.genres,
            lastId = request.lastId,
            size = request.size
        ).toResult(TAG, "exploreOrSearch")
    }

    override suspend fun submitReviews(request: List<RatedAnime>): Result<Unit> {
        return authApi.submitReviews(request).toUnitResult(TAG, "submitReviews")
    }
}