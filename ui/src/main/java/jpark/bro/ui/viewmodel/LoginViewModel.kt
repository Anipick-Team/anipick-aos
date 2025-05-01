package jpark.bro.ui.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jpark.bro.domain.common.ActivityProvider
import jpark.bro.domain.model.User
import jpark.bro.domain.usecase.AuthenticateWithServerUseCase
import jpark.bro.domain.usecase.CheckAuthStatusUseCase
import jpark.bro.domain.usecase.GetAuthStateUseCase
import jpark.bro.domain.usecase.SignInWithGoogleUseCase
import jpark.bro.domain.usecase.SignInWithKakaoUseCase
import jpark.bro.domain.usecase.SignOutUseCase
import jpark.bro.domain.util.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signInWithKakaoUseCase: SignInWithKakaoUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val authenticateWithServerUseCase: AuthenticateWithServerUseCase,
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase,
) : ViewModel() {



    val authState: StateFlow<ApiResult<User>> = getAuthStateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ApiResult.Loading
        )

    private val _serverAuthState = MutableStateFlow<ApiResult<Unit>>(ApiResult.Loading)
    val serverAuthState: StateFlow<ApiResult<Unit>> = _serverAuthState

    // 구글 로그인 시작
    fun signInWithGoogle() {
        viewModelScope.launch {
            val result = signInWithGoogleUseCase()
            // 구글 로그인 성공 시 서버 인증도 함께 진행됨


        }
    }

    // 로그아웃
    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }

    fun signInWithKakao(activity: Activity) {
        viewModelScope.launch {
            val activityProvider = object : ActivityProvider {
                override fun getActivity(): Any = activity
            }
            signInWithKakaoUseCase(activityProvider)
        }
    }
}