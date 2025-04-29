package jpark.bro.anipick.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.qualifiers.ApplicationContext
import jpark.bro.anipick.data.model.UserData
import jpark.bro.anipick.domain.model.User
import jpark.bro.anipick.domain.repository.AuthRepository
import jpark.bro.anipick.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
//    private val apiService: ApiService,
//    private val tokenManager: TokenManager
) : AuthRepository {
    private val _currentUserData = MutableStateFlow<UserData?>(null)
    override val currentUser: Flow<User?> = _currentUserData.map { it?.toDomainModel() }

    // 인증 상태
    private val _authState = MutableStateFlow<Result<UserData>>(Result.Loading)
    override val authState: Flow<Result<User>> = _authState.map { result ->
        when (result) {
            is Result.Success -> Result.Success(result.data.toDomainModel())
            is Result.Error -> Result.Error(result.exception)
            Result.Loading -> Result.Loading
        }
    }

//    override val isAuthenticated: Flow<Boolean> = tokenManager.isTokenValid

    private val WEB_CLIENT_ID = "690192824463-9o6l5f94qfqb66k2i0bitb934m7a3269.apps.googleusercontent.com" // 실제 ID로 교체 필요

    // 자격 증명 관리자 인스턴스
    private val credentialManager by lazy { CredentialManager.create(context) }

    // Google ID 옵션 생성
    private fun createGoogleIdOption(): GetSignInWithGoogleOption {
        return GetSignInWithGoogleOption.Builder(WEB_CLIENT_ID)
            .build()
    }

    // 자격 증명 요청 객체 생성
    private fun createGetCredentialRequest(): GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(createGoogleIdOption())
            .build()
    }

    // Google 로그인 시작
    override suspend fun signInWithGoogle(): Result<User> {
        _authState.value = Result.Loading

        return try {
            // 자격 증명 요청
            val response = credentialManager.getCredential(
                request = createGetCredentialRequest(),
                context = context
            )

            // 응답 처리
            val result = handleSignInResponse(response)
            if (result is Result.Success) {
                _currentUserData.value = result.data

//                // 서버 인증 진행
//                val authResult = authenticateWithServer(result.data.idToken ?: "")
//                if (authResult is Result.Error) {
//                    return Result.Error(authResult.exception)
//                }

                Result.Success(result.data.toDomainModel())
            } else {
                result as Result.Error
                _authState.value = result
                Result.Error(result.exception)
            }
        } catch (e: GetCredentialException) {
            // 오류 처리
            val error = Result.Error(e)
            _authState.value = error
            error
        }
    }

    override suspend fun signInWithKakao(activity: Activity) {
        _authState.value = Result.Loading

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("kakao login", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i("kakao login", "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
            UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
                if (error != null) {
                    Log.e("kakao login", "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)
                } else if (token != null) {
                    Log.i("kakao login", "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)
        }

    }




//    // 서버에 인증 요청
//    override suspend fun authenticateWithServer(idToken: String): Result<Unit> {
//        if (idToken.isEmpty()) {
//            return Result.Error(Exception("ID Token is empty"))
//        }
//
//        return try {
//            val currentUser = _currentUserData.value ?: return Result.Error(Exception("User data not available"))
//
//            // 서버 인증 요청 생성
//            val request = GoogleSignInRequest(
//                idToken = idToken,
//                email = currentUser.id,
//                name = currentUser.displayName,
//                profilePicture = currentUser.profilePictureUrl
//            )
//
//            // 서버 요청
//            val response = apiService.authenticateWithGoogle(request)
//
//            // 토큰 저장
//            tokenManager.saveTokens(
//                accessToken = response.accessToken,
//                refreshToken = response.refreshToken,
//                userId = response.userId,
//                expiresIn = response.expiresIn
//            )
//
//            Result.Success(Unit)
//        } catch (e: IOException) {
//            Result.Error(Exception("Network error occurred", e))
//        } catch (e: Exception) {
//            Result.Error(e)
//        }
//    }
//
//    // 토큰 갱신
//    override suspend fun refreshToken(): Result<Unit> {
//        return try {
//            val refreshToken = tokenManager.refreshToken.first() ?: return Result.Error(Exception("Refresh token not available"))
//
//            val response = apiService.refreshToken(refreshToken)
//
//            tokenManager.saveTokens(
//                accessToken = response.accessToken,
//                refreshToken = response.refreshToken,
//                userId = response.userId,
//                expiresIn = response.expiresIn
//            )
//
//            Result.Success(Unit)
//        } catch (e: Exception) {
//            Result.Error(e)
//        }
//    }

    // 로그아웃
    override suspend fun signOut(): Result<Unit> {
        return try {
            // 로그아웃 로직 (필요한 경우 추가 구현)
            _currentUserData.value = null
            _authState.value = Result.Loading

            // 토큰 삭제
//            tokenManager.clearTokens()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // 로그인 결과 처리
    private fun handleSignInResponse(response: androidx.credentials.GetCredentialResponse): Result<UserData> {
        return try {
            // Google ID 토큰 파싱
            val credential = GoogleIdTokenCredential.createFrom(response.credential.data)
            val idToken = credential.idToken

            // 사용자 정보 추출
            val userId = credential.id                      // Email
            val displayName = credential.displayName ?: ""  // Full Name
            val givenName = credential.givenName ?: ""      // Given Name
            val familyName = credential.familyName ?: ""    // Family Name
            val profilePictureUri = credential.profilePictureUri?.toString() ?: ""

            // 사용자 정보로 UserData 객체 생성
            val userData = UserData(
                id = userId,
                displayName = displayName,
                profilePictureUrl = profilePictureUri,
                idToken = idToken
            )

            // 상태 업데이트
            val success = Result.Success(userData)
            _authState.value = success
            success

        } catch (e: GoogleIdTokenParsingException) {
            val error = Result.Error(e)
            _authState.value = error
            error
        }
    }
}