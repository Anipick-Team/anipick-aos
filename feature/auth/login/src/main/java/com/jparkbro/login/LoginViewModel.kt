package com.jparkbro.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.model.common.Result
import com.jparkbro.domain.GoogleLoginUseCase
import com.jparkbro.domain.KakaoLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val kakaoLoginUseCase: KakaoLoginUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun signInWithGoogle(activity: Activity) {
        socialLoginProceed {
            googleLoginUseCase(activity)
        }
    }

    fun signInWithKakao(activity: Activity) {
        socialLoginProceed {
            kakaoLoginUseCase(activity)
        }
    }

    private fun socialLoginProceed(socialLogin: () -> Flow<Result<Boolean>>) {
        viewModelScope.launch {
            try {
                socialLogin().collect { result ->
                    _uiState.value = when (result) {
                        is Result.Loading -> LoginUiState.Loading
                        is Result.Success<Boolean> -> LoginUiState.Success(result.data)
                        is Result.Error -> LoginUiState.Error(result.exception.message ?: "로그인 중 에러 발생")
                    }
                }
            } catch (e: Exception) {
                // TODO Error
            }
        }
    }

    fun resetUiState() {
        _uiState.value = LoginUiState.Idle
    }
}

sealed interface LoginUiState {
    data object Idle: LoginUiState
    data object Loading: LoginUiState
    data class Success(val reviewCompletedYn: Boolean): LoginUiState
    data class Error(val msg: String): LoginUiState
}