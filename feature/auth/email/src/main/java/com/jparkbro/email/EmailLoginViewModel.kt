package com.jparkbro.email

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.domain.EmailLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.jparkbro.ui.util.extension.filterKorean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EmailLoginViewModel @Inject constructor(
    private val emailLoginUseCase: EmailLoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<EmailLoginUiState>(EmailLoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _emailText = MutableStateFlow("")
    val emailText = _emailText.asStateFlow()

    private val _passwordText = MutableStateFlow("")
    val passwordText = _passwordText.asStateFlow()

    private val _isVisibility = MutableStateFlow(false)
    val isVisibility = _isVisibility.asStateFlow()

    fun updateEmail(email: String) {
        _emailText.value = email
    }

    fun updatePassword(password: String) {
        _passwordText.value = password.filterKorean()
    }

    fun togglePasswordVisibility() {
        _isVisibility.value = !_isVisibility.value
    }

    fun login() {
        _uiState.value = EmailLoginUiState.Loading
        
        viewModelScope.launch {
            try {
                emailLoginUseCase(
                    email = _emailText.value,
                    password = _passwordText.value,
                ).collect { result ->
                    _uiState.value = result.fold(
                        onSuccess = { reviewCompletedYn ->
                            Log.d("EmailLoginViewModel", "Login success: $reviewCompletedYn")
                            EmailLoginUiState.Success(reviewCompletedYn)
                        },
                        onFailure = { exception ->
                            Log.e("EmailLoginViewModel", "Login failed", exception)
                            EmailLoginUiState.Error(exception.message ?: "로그인에 실패했습니다")
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("EmailLoginViewModel", "Unexpected error during login", e)
                _uiState.value = EmailLoginUiState.Error(e.message ?: "예상치 못한 오류가 발생했습니다")
            }
        }
    }
}

sealed interface EmailLoginUiState {
    data object Idle: EmailLoginUiState
    data object Loading: EmailLoginUiState
    data class Success(val reviewCompletedYn: Boolean): EmailLoginUiState
    data class Error(val msg: String): EmailLoginUiState
}