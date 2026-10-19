package com.jparkbro.auth.impl.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.core.common.result.DataError
import com.jparkbro.core.common.result.onFailure
import com.jparkbro.core.common.result.onSuccess
import com.jparkbro.core.common.result.toDisplayMessage
import com.jparkbro.core.data.auth.AuthRepository
import com.jparkbro.core.ui.GlobalSnackbarManager
import com.jparkbro.domain.GoogleLoginUseCase
import com.jparkbro.model.exception.ApiException
import com.jparkbro.model.exception.NetworkException
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val globalSnackbarManager: GlobalSnackbarManager,
    private val googleLoginUseCase: GoogleLoginUseCase,
) : ViewModel() {

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnKakaoLoginClick -> { kakaoLogin(action.activity) }
            is LoginAction.OnGoogleLoginClick -> { googleLogin(action.activity) }
            is LoginAction.Navigation -> Unit // Root에서 처리한다.
        }
    }

    private fun kakaoLogin(activity: Activity) {
        viewModelScope.launch {
            val token = requestKakaoToken(activity) ?: return@launch

            authRepository.loginWithKakao(token.accessToken)
                .onSuccess { reviewCompletedYn ->
                    _events.send(
                        if (reviewCompletedYn == true) LoginEvent.NavigateToHome else LoginEvent.NavigateToPreferenceSetup
                    )
                }
                .onFailure { error ->
                    Timber.e("카카오 로그인 실패: $error")
                    handleLoginFailure(error)
                }
        }
    }

    /** 카카오톡 앱 로그인을 우선 시도하고, 설치돼 있지 않거나 실패하면 카카오계정(웹) 로그인으로 대체한다. */
    private suspend fun requestKakaoToken(activity: Activity): OAuthToken? =
        suspendCancellableCoroutine { continuation ->
            val accountLoginCallback: (OAuthToken?, Throwable?) -> Unit = accountLoginCallback@{ token, error ->
                if (!continuation.isActive) return@accountLoginCallback

                if (error != null) {
                    Timber.e(error, "카카오계정 로그인 실패")
                    continuation.resume(null)
                } else {
                    continuation.resume(token)
                }
            }

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
                UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
                    when {
                        !continuation.isActive -> Unit
                        error != null && error is ClientError && error.reason == ClientErrorCause.Cancelled -> {
                            // 사용자가 카카오톡 로그인 화면에서 직접 취소한 경우: 계정 로그인으로 넘어가지 않고 종료
                            continuation.resume(null)
                        }
                        error != null -> {
                            Timber.e(error, "카카오톡 로그인 실패, 카카오계정으로 재시도")
                            UserApiClient.instance.loginWithKakaoAccount(activity, callback = accountLoginCallback)
                        }
                        else -> continuation.resume(token)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(activity, callback = accountLoginCallback)
            }
        }

    /** 구글 로그인은 v1 경로(core:domain [GoogleLoginUseCase])를 그대로 쓴다. v2 경로(authRepository.loginWithGoogle)는 동작하지 않는다. */
    private fun googleLogin(activity: Activity) {
        viewModelScope.launch {
            googleLoginUseCase(activity).collect { result ->
                result.fold(
                    onSuccess = { reviewCompletedYn ->
                        _events.send(
                            if (reviewCompletedYn) LoginEvent.NavigateToHome else LoginEvent.NavigateToPreferenceSetup
                        )
                    },
                    onFailure = { exception ->
                        Timber.e(exception, "구글 로그인 실패")
                        handleLoginFailure(exception.toNetworkError())
                    },
                )
            }
        }
    }

    private fun handleLoginFailure(error: DataError.Network) {
        globalSnackbarManager.showSnackbar(error.toDisplayMessage())
    }

    /** v1 [GoogleLoginUseCase]가 던지는 예외를 v2 에러 문구 체계([toDisplayMessage])로 옮긴다. */
    private fun Throwable.toNetworkError(): DataError.Network = when (this) {
        is NetworkException -> DataError.Network.NO_INTERNET
        is ApiException -> DataError.Network.Api(code = errorCode, message = errorValue, reason = null)
        else -> DataError.Network.UNKNOWN
    }
}
