package com.jparkbro.findpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.AuthRepository
import com.jparkbro.findpassword.util.RequestCodeButtonUiState
import com.jparkbro.findpassword.util.VerifyButtonUiState
import com.jparkbro.model.auth.RequestCode
import com.jparkbro.model.auth.VerifyCode
import com.jparkbro.model.exception.ApiException
import com.jparkbro.ui.DialogData
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

    /** email TextField */
    private val _emailText = MutableStateFlow("")
    val emailText = _emailText.asStateFlow()

    /** code TextField */
    private val _verificationCodeText = MutableStateFlow("")
    val verificationCodeText = _verificationCodeText.asStateFlow()

    /** email error message */
    private val _emailErrorText = MutableStateFlow<String?>(null)
    val emailErrorText = _emailErrorText.asStateFlow()

    /** verification code message */
    private val _verificationCodeMessage = MutableStateFlow<String?>(null)
    val verificationCodeMessage = _verificationCodeMessage.asStateFlow()

    /** dialog */
    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private val _requestCodeButtonState = MutableStateFlow<RequestCodeButtonUiState>(RequestCodeButtonUiState.Initial)
    val requestCodeButtonState = _requestCodeButtonState.asStateFlow()
    // 타이머 Job (인증번호받기 버튼용)
    private var requestCodeTimerJob: Job? = null

    private val _verifyButtonState = MutableStateFlow<VerifyButtonUiState>(VerifyButtonUiState.Inactive)
    val verifyButtonState = _verifyButtonState.asStateFlow()
    // 타이머 Job (인증하기 버튼용)
    private var verifyTimerJob: Job? = null

    fun updateEmail(email: String) {
        _emailText.value = email
    }

    fun updateVerificationCode(verificationCode: String) {
        _verificationCodeText.value = verificationCode
    }

    fun dismissDialog() {
        _showDialog.value = false
    }

    fun requestVerificationCode(onValid: (Boolean) -> Unit) {
        /* 1. 이메일 유효성 검증 */
        val emailErrorMessage = EmailValidator.getErrorMessage(_emailText.value)
        if (emailErrorMessage != null) {
            _emailErrorText.value = emailErrorMessage
            onValid(false)
            return
        }

        _emailErrorText.value = null
        onValid(true)

        /* 2. 버튼 비활성화 (API 요청 시작) */
        _requestCodeButtonState.value = RequestCodeButtonUiState.Loading

        /* 3. 인증코드 발송 API */
        viewModelScope.launch {
            authRepository.requestResetCode(
                RequestCode(_emailText.value)
            ).fold(
                onSuccess = {
                    _verificationCodeMessage.value = "수신하신 인증번호를 입력해 주세요."
                    startRequestCodeCountDown()
                    startVerifyCountdown()
                },
                onFailure = { exception ->
                    _requestCodeButtonState.value = RequestCodeButtonUiState.Initial
                    when (exception) {
                        is ApiException -> {
                            if (exception.errorCode == 122) {
                                _showDialog.value = true
                            } else {
                                _emailErrorText.value = exception.errorValue
                            }
                        }
                        else -> {
                            // TODO Api 통신에러
                        }
                    }
                }
            )
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

            _verifyButtonState.value = VerifyButtonUiState.Expired
            _verificationCodeMessage.value = "유효 시간이 만료되었습니다. 재발송 후 다시 시도해주세요."
        }
    }

    fun verifyCode(onSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.verifyResetCode(
                VerifyCode(
                    email = _emailText.value,
                    code = _verificationCodeText.value
                )
            ).fold(
                onSuccess = {
                    verifyTimerJob?.cancel()
                    _verifyButtonState.value = VerifyButtonUiState.Success
                    onSuccess()
                },
                onFailure = { exception ->
                    when (exception) {
                        is ApiException -> {
                            _verificationCodeMessage.value = exception.errorValue
                        }
                        else -> {

                        }
                    }
                }
            )
        }
    }
}