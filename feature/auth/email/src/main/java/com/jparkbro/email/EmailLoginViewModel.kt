package com.jparkbro.email

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.domain.EmailLoginUseCase
import com.jparkbro.model.common.Result
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
        viewModelScope.launch {
            try {
                emailLoginUseCase(
                    email = _emailText.value,
                    password = _passwordText.value,
                ).collect { result ->
                    _uiState.value = when (result) {
                        is Result.Loading -> EmailLoginUiState.Loading
                        is Result.Success<Boolean> -> EmailLoginUiState.Success(result.data)
                        is Result.Error -> EmailLoginUiState.Error("${result.exception.message}")
                    }
                    // TODO 예외처리
                }
            } catch (e: Exception) {
                _uiState.value = EmailLoginUiState.Error("${e.message}")
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