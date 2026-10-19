package com.jparkbro.network.auth

import android.util.Log
import com.jparkbro.core.network.constructRoute
import com.jparkbro.model.auth.AuthResponse
import com.jparkbro.model.auth.LoginProvider
import com.jparkbro.model.auth.SocialLoginRequest
import com.jparkbro.model.exception.ApiException
import com.jparkbro.model.exception.NetworkException
import com.jparkbro.network.model.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.http.isSuccess
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import java.io.IOException

/**
 * network data source 구현체
 * 요청/응답 JSON 설정과 실패 처리는 Retrofit으로 호출하던 때와 같다.
 */
internal class KtorAuthDataSource(
    private val httpClient: HttpClient,
    private val json: Json,
) : AuthDataSource {
    companion object {
        private const val TAG = "KtorAuthDataSource"
    }

    override suspend fun socialLogin(provider: LoginProvider, socialToken: String): Result<AuthResponse> {
        Log.d(TAG, "socialLogin() called")
        return try {
            val request = SocialLoginRequest(platform = "android", code = socialToken)
            val response = httpClient.post(constructRoute("/oauth/${provider.value}/callback")) {
                setBody(TextContent(json.encodeToString(request), ContentType.Application.Json))
            }
            Log.d(TAG, "socialLogin() response - code: ${response.status.value}")

            if (!response.status.isSuccess()) {
                Log.e(TAG, "socialLogin() HTTP error - code: ${response.status.value}")
                return Result.failure(IllegalStateException("HTTP ${response.status.value} ${response.status.description}"))
            }

            val apiResponse = json.decodeFromString<ApiResponse<AuthResponse>>(response.bodyAsText())
            if (apiResponse.value == "success" && apiResponse.result != null) {
                Log.d(TAG, "socialLogin() success")
                Result.success(apiResponse.result)
            } else {
                Log.e(TAG, "socialLogin() API error - $apiResponse")
                Result.failure(ApiException(apiResponse.code, apiResponse.errorValue))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Log.e(TAG, "socialLogin() network error", e)
            Result.failure(NetworkException())
        } catch (e: UnresolvedAddressException) {
            Log.e(TAG, "socialLogin() network error", e)
            Result.failure(NetworkException())
        } catch (e: Exception) {
            Log.e(TAG, "socialLogin() exception", e)
            Result.failure(e)
        }
    }
}
