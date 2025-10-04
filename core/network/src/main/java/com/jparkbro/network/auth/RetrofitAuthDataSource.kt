package com.jparkbro.network.auth

import android.util.Log
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
import com.jparkbro.model.auth.SocialLoginRequest
import com.jparkbro.model.auth.VerifyCode
import com.jparkbro.network.util.toResult
import com.jparkbro.network.util.toUnitResult
import retrofit2.HttpException
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

    override suspend fun emailSignup(request: SignupRequest): Result<AuthToken> {
        Log.d(TAG, "emailSignup() called - email: ${request.email}")
        return try {
            val response = authApi.emailSignup(request)
            Log.d(TAG, "emailSignup() response received - isSuccessful: ${response.isSuccessful}, code: ${response.code()}")

            val apiResponse = response.body()
            Log.d(TAG, "emailSignup() apiResponse - value: ${apiResponse?.value}, code: ${apiResponse?.code}")

            when {
                // retrofit error (200번대 이외)
                !response.isSuccessful -> {
                    Log.e(TAG, "emailSignup() HTTP error - code: ${response.code()}, message: ${response.message()}")
                    Result.failure(HttpException(response))
                }

                apiResponse?.value == "success" && apiResponse.result != null -> {
                    val authToken = apiResponse.result.token
                    Log.d(TAG, "emailSignup() success - token received")
                    Result.success(authToken)
                }

                // value = fail, result == null
                else -> {
                    Log.e(TAG, "emailSignup() API error - code: ${apiResponse?.code}, reason: ${apiResponse?.errorReason}")
                    Result.failure(Exception(apiResponse?.errorValue))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "emailSignup() exception", e)
            Result.failure(e)
        }
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

    override suspend fun exploreOrSearch(request: PreferenceRequest): Result<PreferenceResponse> {
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