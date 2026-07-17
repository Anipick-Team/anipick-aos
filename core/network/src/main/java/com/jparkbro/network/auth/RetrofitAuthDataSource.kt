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
import com.jparkbro.network.util.safeApiCall
import com.jparkbro.network.util.safeApiCallUnit
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
        return safeApiCall(TAG, "socialLogin") {
            authApi.socialLogin(
                provider = provider.value,
                request = SocialLoginRequest(
                    platform = "android",
                    code = socialToken
                )
            )
        }
    }

    override suspend fun emailSignup(request: EmailRegisterRequest): Result<EmailRegisterResponse> {
        return safeApiCall(TAG, "emailSignup") { authApi.emailSignup(request) }
    }


    override suspend fun emailLogin(request: EmailLoginRequest): Result<AuthResponse> {
        return safeApiCall(TAG, "emailLogin") { authApi.emailLogin(request) }
    }

    override suspend fun requestResetCode(request: RequestCode): Result<Unit> {
        return safeApiCallUnit(TAG, "requestResetCode") { authApi.requestResetCode(request) }
    }

    override suspend fun verifyResetCode(request: VerifyCode): Result<Unit> {
        return safeApiCallUnit(TAG, "verifyResetCode") { authApi.verifyResetCode(request) }
    }

    override suspend fun resetPassword(request: ResetPassword): Result<Unit> {
        return safeApiCallUnit(TAG, "resetPassword") { authApi.resetPassword(request) }
    }

    override suspend fun exploreOrSearch(request: SearchRequest): Result<SearchResponse> {
        return safeApiCall(TAG, "exploreOrSearch") {
            authApi.exploreOrSearch(
                query = request.query,
                year = request.year,
                season = request.season,
                genres = request.genres,
                lastId = request.lastId,
                size = request.size
            )
        }
    }

    override suspend fun submitReviews(request: List<RatedAnime>): Result<Unit> {
        return safeApiCallUnit(TAG, "submitReviews") { authApi.submitReviews(request) }
    }
}
