package jpark.bro.data

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialRequest.Builder
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import jpark.bro.model.JwtToken
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(

) : AuthRepository {
    override suspend fun emailSignup() {
        TODO("Not yet implemented")
    }

    override suspend fun emailLogin(): Result<JwtToken> {
        TODO("Not yet implemented")
    }

    override suspend fun getKakaoAuthToken(activity: Activity): Result<String> {
        // 결과를 저장할 CompletableDeferred 객체 생성
        val deferred = CompletableDeferred<Result<String>>()

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                deferred.complete(Result.failure(error))
            } else if (token != null) {
                deferred.complete(Result.success(token.accessToken))
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
            UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
                if (error != null) {
                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        deferred.complete(Result.failure(error))
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)
                } else if (token != null) {
                    deferred.complete(Result.success(token.accessToken))
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)
        }

        // 비동기 작업의 결과를 반환
        return deferred.await()
    }

    /*
    * Google API Get Token
    */
    private val WEB_CLIENT_ID = "690192824463-9o6l5f94qfqb66k2i0bitb934m7a3269.apps.googleusercontent.com" // TODO 실제 ID로 교체 필요

    // Trigger a Sign in with Google button flow
    private val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption
        .Builder(serverClientId = WEB_CLIENT_ID)
        .build()

    private val request: GetCredentialRequest = Builder()
        .addCredentialOption(credentialOption = signInWithGoogleOption)
        .build()

    override suspend fun getGoogleAuthToken(activity: Activity): Result<String> {
        return try {
            val result = CredentialManager.create(activity).getCredential(
                request = request,
                context = activity,
            )
            handleSignIn(result)
        } catch (e: GetCredentialException) {
            handleFailure(e)
        }
    }

    private fun handleSignIn(result: GetCredentialResponse): Result<String> {
        val credential = result.credential

        return when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        Result.success(googleIdTokenCredential.idToken) // ID Token 반환
                    } catch (e: GoogleIdTokenParsingException) {
                        Result.failure(e)
                    }
                } else {
                    // Catch any unrecognized credential type here.
                    Result.failure<String>(IllegalStateException("Unrecognized credential type"))
                }
            }
            else -> {
                // Catch any unrecognized credential type here.
                Result.failure<String>(IllegalStateException("Unrecognized credential type"))
            }
        }
    }

    private fun handleFailure(e: GetCredentialException): Result<String> {
        return Result.failure(e)
    }

    override suspend fun socialLogin(socialToken: String): Result<JwtToken> {
        TODO("Not yet implemented")
    }
}