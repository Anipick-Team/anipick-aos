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
        Log.d(TAG, "socialLogin() called - provider: ${provider.value}")

        return try {
            val response = authApi.socialLogin(
                provider = provider.value,
                request = SocialLoginRequest(
                    platform = "android",
                    code = socialToken,
                )
            )
            Log.d(TAG, "socialLogin() response received - isSuccessful: ${response.isSuccessful}, code: ${response.code()}")

            val apiResponse = response.body()
            Log.d(TAG, "socialLogin() apiResponse - result: ${apiResponse?.result}")

            when {
                // retrofit error (200번대 이외)
                !response.isSuccessful -> {
                    Log.e(TAG, "socialLogin() HTTP error - code: ${response.code()}, message: ${response.message()}")
                    Result.failure<AuthResponse>(HttpException(response))
                }

                apiResponse?.value == "success" && apiResponse.result != null -> {
                    Log.d(TAG, "socialLogin() success - result: ${apiResponse.result}")
                    Result.success<AuthResponse>(apiResponse.result)
                }

                // value = fail, result == null
                else -> {
                    Log.e(TAG, "socialLogin() API error - code: ${apiResponse?.code}")
                    Result.failure(Exception("$apiResponse"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "socialLogin() exception", e)
            Result.failure(e)
        }
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
                    Result.failure<AuthToken>(HttpException(response))
                }

                apiResponse?.value == "success" && apiResponse.result != null -> {
                    val authToken = apiResponse.result.token
                    Log.d(TAG, "emailSignup() success - token received")
                    Result.success<AuthToken>(authToken)
                }

                // value = fail, result == null
                else -> {
                    Log.e(TAG, "emailSignup() API error - code: ${apiResponse?.code}")
                    Result.failure<AuthToken>(Exception("${apiResponse?.code}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "emailSignup() exception", e)
            Result.failure(e)
        }
    }


    override suspend fun emailLogin(request: EmailLoginRequest): Result<AuthResponse> {
        Log.d(TAG, "emailLogin() called - email: ${request.email}")
        return try {
            val response = authApi.emailLogin(request)
            Log.d(TAG, "emailLogin() response received - isSuccessful: ${response.isSuccessful}, code: ${response.code()}")

            val apiResponse = response.body()
            Log.d(TAG, "emailLogin() apiResponse - result: ${apiResponse?.result}")

            when {
                // retrofit error (200번대 이외)
                !response.isSuccessful -> {
                    Log.e(TAG, "emailLogin() HTTP error - code: ${response.code()}, message: ${response.message()}")
                    Result.failure<AuthResponse>(HttpException(response))
                }

                apiResponse?.value == "success" && apiResponse.result != null -> {
                    Log.d(TAG, "emailLogin() success - result received")
                    Result.success(apiResponse.result)
                }

                // value = fail, result == null
                else -> {
                    Log.e(TAG, "emailLogin() API error - $apiResponse")
                    Result.failure(Exception("$apiResponse"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "emailLogin() exception", e)
            Result.failure(e)
        }
    }

    override suspend fun requestResetCode(request: RequestCode): Result<Unit> {
        Log.d(TAG, "requestResetCode() called - email: $request")
        return try {
            val response = authApi.requestResetCode(request)
            Log.d(TAG, "requestResetCode() response received - isSuccessful: ${response.isSuccessful}, code: ${response.code()}")

            val apiResponse = response.body()
            Log.d(TAG, "requestResetCode() apiResponse - value: ${apiResponse?.value}, code: ${apiResponse?.code}")
            Log.d(TAG, "verifyResetCode() apiResponse - value: ${apiResponse?.errorValue}")
            Log.d(TAG, "verifyResetCode() apiResponse - value: ${apiResponse?.errorReason}")

            when {
                // retrofit error (200번대 이외)
                !response.isSuccessful -> {
                    Log.e(TAG, "requestResetCode() HTTP error - code: ${response.code()}, message: ${response.message()}")
                    Result.failure<Unit>(HttpException(response))
                }

                apiResponse?.value == "success" -> {
                    Log.d(TAG, "requestResetCode() success")
                    Result.success(Unit)
                }

                // value = fail
                else -> {
                    Log.e(TAG, "requestResetCode() API error - code: ${apiResponse?.code}")
                    Result.failure<Unit>(Exception("${apiResponse?.code}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "requestResetCode() exception", e)
            Result.failure(e)
        }
    }

    override suspend fun verifyResetCode(request: VerifyCode): Result<Unit> {
        Log.d(TAG, "verifyResetCode() called - email: ${request.email}, code: ${request.code}")
        return try {
            val response = authApi.verifyResetCode(request)
            Log.d(TAG, "verifyResetCode() response received - isSuccessful: ${response.isSuccessful}, code: ${response.code()}")

            val apiResponse = response.body()
            Log.d(TAG, "verifyResetCode() apiResponse - value: ${apiResponse?.value}, code: ${apiResponse?.code}")


            when {
                // retrofit error (200번대 이외)
                !response.isSuccessful -> {
                    Log.e(TAG, "verifyResetCode() HTTP error - code: ${response.code()}, message: ${response.message()}")
                    Result.failure(HttpException(response))
                }

                apiResponse?.value == "success" -> {
                    Log.d(TAG, "verifyResetCode() success")
                    Result.success(Unit)
                }

                // value = fail
                else -> {
                    Log.e(TAG, "verifyResetCode() API error - code: ${apiResponse?.code}")
                    Result.failure<Unit>(Exception("${apiResponse?.code}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "verifyResetCode() exception", e)
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(request: ResetPassword): Result<Unit> {
        Log.d(TAG, "resetPassword() called - email: $request")
        return try {
            val response = authApi.resetPassword(request)
            Log.d(TAG, "resetPassword() response received - isSuccessful: ${response.isSuccessful}, code: ${response.code()}")

            val apiResponse = response.body()
            Log.d(TAG, "resetPassword() apiResponse - value: ${apiResponse?.value}, code: ${apiResponse?.code}")

            when {
                // retrofit error (200번대 이외)
                !response.isSuccessful -> {
                    Log.e(TAG, "resetPassword() HTTP error - code: ${response.code()}, message: ${response.message()}")
                    Result.failure<Unit>(HttpException(response))
                }

                apiResponse?.value == "success" -> {
                    Log.d(TAG, "resetPassword() success")
                    Result.success(Unit)
                }

                // value = fail
                else -> {
                    Log.e(TAG, "resetPassword() API error - code: ${apiResponse?.code}")
                    Result.failure(Exception("${apiResponse?.code}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "resetPassword() exception", e)
            Result.failure(e)
        }
    }

    override suspend fun exploreOrSearch(request: PreferenceRequest): Result<PreferenceResponse> {
        Log.d(TAG, "exploreAnimes() called")

        return try {
            val response = authApi.exploreOrSearch(
                query = request.query,
                year = request.year,
                season = request.season,
                genres = request.genres,
                lastId = request.lastId,
                size = request.size
            )
            Log.d(TAG, "exploreAnimes() response received - isSuccessful: ${response.isSuccessful}, code: ${response.code()}")

            val apiResponse = response.body()
            Log.d(TAG, "exploreAnimes() apiResponse - value: ${apiResponse?.value}, code: ${apiResponse?.code}")

            when {
                // retrofit error (200번대 이외)
                !response.isSuccessful -> {
                    Log.e(TAG, "exploreAnimes() HTTP error - code: ${response.code()}, message: ${response.message()}")
                    Result.failure(HttpException(response))
                }

                apiResponse?.value == "success" && apiResponse.result != null -> {
                    Log.d(TAG, "exploreAnimes() success")
                    Result.success(apiResponse.result)
                }

                // value = fail
                else -> {
                    Log.e(TAG, "exploreAnimes() API error - code: ${apiResponse?.code}")
                    Result.failure(Exception("${apiResponse?.code}"))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "exploreAnimes() exception", e)
            Result.failure(e)
        }
    }

    override suspend fun submitReviews(request: List<RatedAnime>): Result<Unit> {
        return authApi.submitReviews(request).toUnitResult(TAG, "submitReviews")
    }
}