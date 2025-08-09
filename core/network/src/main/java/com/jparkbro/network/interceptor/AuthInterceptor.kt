package com.jparkbro.network.interceptor

import android.util.Log
import com.jparkbro.model.auth.AuthToken
import com.jparkbro.network.auth.AuthApi
import com.jparkbro.network.model.ApiResponse
import com.jparkbro.network.repository.TokenRepository
import com.jparkbro.network.retrofit.ApiConstants
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository
) : Interceptor {
    companion object {
        private const val TAG = "AuthInterceptor"
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val TOKEN_TYPE = "Bearer"
    }

    // 토큰 갱신 중복 방지를 위한 뮤텍스
    private val refreshTokenMutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 1. auth 관련 API는 토큰 없이 요청
        val isAuthApi = originalRequest.url.encodedPath.let { path ->
            path.contains("/oauth/") ||
                    path.contains("/users/signup") ||
                    path.contains("/users/login") ||
                    path.contains("/auth/email/") ||
                    path.contains("/auth/password/") ||
                    path.contains("/tokens/refresh") ||
                    path.contains("/animes/meta-data-group")
        }

        if (isAuthApi) {
            return chain.proceed(originalRequest)
        }

        // 2. 토큰이 필요한 API 처리
        return runBlocking {
            executeWithAuth(chain, originalRequest)
        }
    }

    private suspend fun executeWithAuth(chain: Interceptor.Chain, originalRequest: okhttp3.Request): Response {
        val result = tokenRepository.getAccessToken()

        val authenticationRequest = when {
            result.isFailure -> {
                Log.w(TAG, "액세스 토큰 가져오기 실패: ${result.exceptionOrNull()?.message}")
                originalRequest
            }

            result.getOrNull().isNullOrEmpty() -> {
                Log.w(TAG, "액세스 토큰이 없음 - 그대로 요청 진행")
                originalRequest
            }

            else -> {
                val accessToken = result.getOrNull()
                originalRequest.newBuilder()
                    .addHeader(HEADER_AUTHORIZATION, "$TOKEN_TYPE $accessToken")
                    .build()
            }
        }

        val response = chain.proceed(authenticationRequest)

        // Token 만료 에러 포함
        if (response.code == 401 || response.code == 403) {
            Log.d(TAG, "토큰 만료 감지 (${response.code}) - 토큰 갱신 시도")
            response.close()
            return refreshTokenAndRetry(chain, originalRequest)
        }

        return response
    }

    private suspend fun refreshTokenAndRetry(chain: Interceptor.Chain, originalRequest: okhttp3.Request): Response {
        return refreshTokenMutex.withLock {
            val refreshResult = tokenRepository.getRefreshToken()

            when {
                refreshResult.isFailure -> {
                    Log.e(TAG, "에러 발생: ${refreshResult.exceptionOrNull()?.message}")
                    // 에러 (로그아웃 처리)
                    return@withLock chain.proceed(originalRequest)
                }

                else -> {
                    val refreshToken = refreshResult.getOrNull()
                    if (refreshToken.isNullOrEmpty()) {
                        // 토큰 없음 로그아웃 처리
                        Log.e(TAG, "저장된 토큰 없음")
                        return@withLock chain.proceed(originalRequest)
                    } else {
                        Log.d(TAG, "Refresh Token Get 성공 - Token 재요청 진행")

                        val refreshRequest = okhttp3.Request.Builder()
                            .url("${ApiConstants.BASE_URL}${ApiConstants.TOKEN_REFRESH}")
                            .post(
                                okhttp3.RequestBody.create(
                                    "application/json".toMediaType(),
                                    "{}"
                                )
                            )
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Bearer $refreshToken")
                            .build()

                        val refreshResponse = chain.proceed(refreshRequest)

                        if (refreshResponse.isSuccessful) {
                            val newToken = parseAccessTokenFromResponse(responseBody = refreshResponse.body?.string())
                            refreshResponse.close()

                            if (newToken != null) {
                                tokenRepository.saveToken(newToken)
                                Log.d(TAG, "API 재요청")
                                val newRequest = originalRequest.newBuilder()
                                    .removeHeader(HEADER_AUTHORIZATION)
                                    .addHeader(HEADER_AUTHORIZATION, "$TOKEN_TYPE ${newToken.accessToken}")
                                    .build()
                                return@withLock chain.proceed(newRequest)
                            }
                        } else {
                            Log.e(TAG, "토큰 갱신 요청 실패 - code: ${refreshResponse.code}")
                            refreshResponse.close()
                        }
                        return@withLock chain.proceed(originalRequest)
                    }
                }
            }
        }
    }

    private fun parseAccessTokenFromResponse(responseBody: String?): AuthToken? {
        return try {
            if (responseBody.isNullOrEmpty()) return null

            val json = Json { ignoreUnknownKeys = true }
            val apiResponse = json.decodeFromString<ApiResponse<AuthToken>>(responseBody)

            if (apiResponse.code == 200 && apiResponse.result != null) {
                apiResponse.result
            } else {
                Log.e(TAG, "API 응답 에러: ${apiResponse.value}")
                null
            }

        } catch (e: Exception) {
            Log.e(TAG, "JSON 파싱 에러: ${e.message}")
            null
        }
    }

    private fun logout() {

    }
}