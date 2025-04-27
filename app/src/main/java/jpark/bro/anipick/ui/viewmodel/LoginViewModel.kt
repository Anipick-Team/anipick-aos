package jpark.bro.anipick.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jpark.bro.anipick.data.model.SignInState
import jpark.bro.anipick.domain.model.User
import jpark.bro.anipick.domain.usecase.AuthenticateWithServerUseCase
import jpark.bro.anipick.domain.usecase.CheckAuthStatusUseCase
import jpark.bro.anipick.domain.util.Result
import jpark.bro.anipick.domain.usecase.GetAuthStateUseCase
import jpark.bro.anipick.domain.usecase.SignInWithGoogleUseCase
import jpark.bro.anipick.domain.usecase.SignOutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val authenticateWithServerUseCase: AuthenticateWithServerUseCase,
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase,
) : ViewModel() {

    val authState: StateFlow<Result<User>> = getAuthStateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Result.Loading
        )

//    val isAuthenticated: StateFlow<Boolean> = checkAuthStatusUseCase()
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.Lazily,
//            initialValue = false
//        )

    private val _serverAuthState = MutableStateFlow<Result<Unit>>(Result.Loading)
    val serverAuthState: StateFlow<Result<Unit>> = _serverAuthState

    // 구글 로그인 시작
    fun signInWithGoogle() {
        viewModelScope.launch {
            val result = signInWithGoogleUseCase()
            // 구글 로그인 성공 시 서버 인증도 함께 진행됨
        }
    }

    // 서버에만 인증 요청 (토큰이 만료되었을 때 등)
//    fun authenticateWithServer(idToken: String) {
//        viewModelScope.launch {
//            _serverAuthState.value = Result.Loading
//            _serverAuthState.value = authenticateWithServerUseCase(idToken)
//        }
//    }

    // 로그아웃
    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }
}