package com.jparkbro.email

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.domain.EmailLoginUseCase
import com.jparkbro.model.exception.ApiException
import com.jparkbro.ui.DialogData
import com.jparkbro.ui.DialogType
import com.jparkbro.ui.util.EmailValidator
import com.jparkbro.ui.util.extension.filterKorean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EmailLoginViewModel @Inject constructor(
    private val emailLoginUseCase: EmailLoginUseCase
) : ViewModel() {

    /** Login Api 통신 상태 */
    private val _uiState = MutableStateFlow<EmailLoginUiState>(EmailLoginUiState.Idle)
    val uiState = _uiState.asStateFlow()

    /** email TextField */
    private val _emailText = MutableStateFlow("")
    val emailText = _emailText.asStateFlow()

    /** password TextField */
    private val _passwordText = MutableStateFlow("")
    val passwordText = _passwordText.asStateFlow()

    /** password visibility */
    private val _isVisibility = MutableStateFlow(false)
    val isVisibility = _isVisibility.asStateFlow()

    /** email valid message */
    private val _emailErrorMessage = MutableStateFlow<String?>(null)
    val emailErrorMessage = _emailErrorMessage.asStateFlow()

    /** login fail message */
    private val _loginFailMessage = MutableStateFlow<String?>(null)
    val loginFailMessage = _loginFailMessage.asStateFlow()

    private val _showDialog = MutableStateFlow<DialogData?>(null)
    val showDialog = _showDialog.asStateFlow()

    fun updateEmail(email: String) {
        _emailText.value = email
    }

    fun updatePassword(password: String) {
        _passwordText.value = password.filterKorean()
    }

    fun togglePasswordVisibility() {
        _isVisibility.value = !_isVisibility.value
    }

    fun dismissDialog() {
        _showDialog.value = null
    }

    fun emailValid() {
        _emailErrorMessage.value = null
        _loginFailMessage.value = null

        val emailErrorMessage = EmailValidator.getErrorMessage(_emailText.value)

        if (emailErrorMessage == null) {
            login()
        } else {
            _emailErrorMessage.value = emailErrorMessage
        }
    }

    private fun login() {
        _uiState.value = EmailLoginUiState.Loading
        
        viewModelScope.launch {
            try {
                emailLoginUseCase(
                    email = _emailText.value,
                    password = _passwordText.value,
                ).collect { result ->
                    _uiState.value = result.fold(
                        onSuccess = { reviewCompletedYn ->
                            EmailLoginUiState.Success(reviewCompletedYn)
                        },
                        onFailure = { exception ->
                            when (exception) {
                                is ApiException -> {
                                    if (exception.errorCode == 101 || exception.errorCode == 104 || exception.errorCode == 106) {
                                        _loginFailMessage.value = exception.errorValue
                                    } else if (exception.errorCode == 132) {
                                        _showDialog.value = DialogData(
                                            type = DialogType.ALERT,
                                            title = "탈퇴된 계정입니다.",
                                            dismiss = "닫기",
                                            errorMsg = "자세한 사항은 고객센터로 문의해 주세요.\nteamanipick@gmail.com",
                                            onDismiss = { dismissDialog() }
                                        )
                                    } else {
                                        _emailErrorMessage.value = exception.errorValue
                                    }
                                }
                                else -> exception.message ?: "로그인에 실패했습니다"
                            }
                            EmailLoginUiState.Idle
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
    data class Error(val msg: String): EmailLoginUiState // HTTP Error 시 동작
}