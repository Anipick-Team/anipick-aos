package com.jparkbro.findpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.AuthRepository
import com.jparkbro.findpassword.util.RequestCodeButtonUiState
import com.jparkbro.findpassword.util.VerifyButtonUiState
import com.jparkbro.model.auth.RequestCode
import com.jparkbro.model.auth.VerifyCode
import dagger.hilt.android.lifecycle.HiltViewModel
import com.jparkbro.ui.util.EmailValidator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordVerificationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _emailText = MutableStateFlow<String>("")
    val emailText: StateFlow<String> = _emailText.asStateFlow()

    private val _verificationCodeText = MutableStateFlow<String>("")
    val verificationCodeText: StateFlow<String> = _verificationCodeText.asStateFlow()

    private val _emailErrorText = MutableStateFlow<String?>(null)
    val emailErrorText: StateFlow<String?> = _emailErrorText.asStateFlow()

    private val _verificationCodeErrorText = MutableStateFlow<String?>(null)
    val verificationCodeErrorText: StateFlow<String?> = _verificationCodeErrorText.asStateFlow()

    private val _requestCodeButtonState = MutableStateFlow<RequestCodeButtonUiState>(RequestCodeButtonUiState.Initial)
    val requestCodeButtonState: StateFlow<RequestCodeButtonUiState> = _requestCodeButtonState.asStateFlow()
    // 타이머 Job (인증번호받기 버튼용)
    private var requestCodeTimerJob: Job? = null

    private val _verifyButtonState = MutableStateFlow<VerifyButtonUiState>(VerifyButtonUiState.Active)
    val verifyButtonState: StateFlow<VerifyButtonUiState> = _verifyButtonState.asStateFlow()
    // 타이머 Job (인증하기 버튼용)
    private var verifyTimerJob: Job? = null

    fun updateEmail(email: String) {
        _emailText.value = email
    }

    fun updateVerificationCode(verificationCode: String) {
        _verificationCodeText.value = verificationCode
    }

    fun requestVerificationCode() {
        _emailErrorText.value = EmailValidator.getErrorMessage(_emailText.value)
        if (!EmailValidator.validate(_emailText.value)) {
            return
        }
        // 1. API 요청
        viewModelScope.launch {
            authRepository.requestResetCode(
                RequestCode(_emailText.value)
            ).fold(
                onSuccess = {

                },
                onFailure = {

                }
            )
        }

        startRequestCodeCountDown()

        if (_verifyButtonState.value !is VerifyButtonUiState.Counting) {
            startVerifyCountdown()
        }
    }

    private fun startRequestCodeCountDown(seconds: Int = 30) {
        requestCodeTimerJob?.cancel()

        requestCodeTimerJob = viewModelScope.launch {
            for (i in seconds downTo 1) {
                _requestCodeButtonState.value = RequestCodeButtonUiState.Counting(i)
                delay(1000)
            }
            _requestCodeButtonState.value = RequestCodeButtonUiState.Ready
        }
    }

    private fun startVerifyCountdown(seconds: Int = 180) {
        verifyTimerJob?.cancel()

        verifyTimerJob = viewModelScope.launch {
            for (i in seconds downTo 1) {
                _verifyButtonState.value = VerifyButtonUiState.Counting(i)
                delay(1000)
            }

            _verifyButtonState.value = VerifyButtonUiState.Active
        }
    }

    fun verifyCode(onSuccess: () -> Unit) {
        viewModelScope.launch {
            // TODO 인증번호 확인 API
            authRepository.verifyResetCode(
                VerifyCode(
                    email = _emailText.value,
                    code = _verificationCodeText.value
                )
            ).fold(
                onSuccess = {
                    // TODO
                    verifyTimerJob?.cancel()
                    _verifyButtonState.value = VerifyButtonUiState.Success
                    onSuccess()
                },
                onFailure = {
                    // TODO
                }
            )

            // TODO _verificationCodeErrorText
        }
    }
}