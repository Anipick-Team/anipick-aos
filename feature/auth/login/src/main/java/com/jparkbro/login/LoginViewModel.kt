package com.jparkbro.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.domain.GoogleLoginUseCase
import com.jparkbro.domain.KakaoLoginUseCase
import com.jparkbro.model.exception.ApiException
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

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    fun dismissDialog() {
        _showDialog.value = false
    }

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
        _uiState.value = LoginUiState.Loading
        
        viewModelScope.launch {
            try {
                socialLogin().collect { result ->
                    _uiState.value = result.fold(
                        onSuccess = { reviewCompletedYn ->
                            LoginUiState.Success(reviewCompletedYn)
                        },
                        onFailure = { exception ->
                            if (exception is ApiException && exception.errorCode == 133) {
                                _showDialog.value = true
                            }
                            LoginUiState.Error(exception.message ?: "로그인 중 에러 발생")
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error(e.message ?: "예상치 못한 오류가 발생했습니다")
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