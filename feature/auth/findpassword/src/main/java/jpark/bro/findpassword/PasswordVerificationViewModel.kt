package jpark.bro.findpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jpark.bro.ui.util.EmailValidator
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordVerificationViewModel @Inject constructor(

) : ViewModel() {

    private val _emailText = MutableStateFlow<String>("")
    val emailText: StateFlow<String> = _emailText

    private val _verificationCodeText = MutableStateFlow<String>("")
    val verificationCodeText: StateFlow<String> = _verificationCodeText

    private val _emailErrorText = MutableStateFlow<String?>(null)
    val emailErrorText: StateFlow<String?> = _emailErrorText

    private val _verificationCodeErrorText = MutableStateFlow<String?>(null)
    val verificationCodeErrorText: StateFlow<String?> = _verificationCodeErrorText

    private val _requestCodeButtonState = MutableStateFlow<RequestCodeButtonUiState>(RequestCodeButtonUiState.Initial)
    val requestCodeButtonState: StateFlow<RequestCodeButtonUiState> = _requestCodeButtonState
    // 타이머 Job (인증번호받기 버튼용)
    private var requestCodeTimerJob: Job? = null

    private val _verifyButtonState = MutableStateFlow<VerifyButtonUiState>(VerifyButtonUiState.Active)
    val verifyButtonState: StateFlow<VerifyButtonUiState> = _verifyButtonState
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

    fun verifyCode(code: String) {
        viewModelScope.launch {
            // TODO 인증번호 확인 API

            verifyTimerJob?.cancel()

            _verifyButtonState.value = VerifyButtonUiState.Success

            // TODO _verificationCodeErrorText
        }
    }
}