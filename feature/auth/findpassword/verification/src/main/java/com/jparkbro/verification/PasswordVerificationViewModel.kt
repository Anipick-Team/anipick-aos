package com.jparkbro.verification

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.AuthRepository
import com.jparkbro.model.auth.RequestCode
import com.jparkbro.model.auth.VerifyCode
import com.jparkbro.model.enum.DialogType
import com.jparkbro.model.exception.ApiException
import com.jparkbro.ui.R
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.util.UiText
import com.jparkbro.util.UserDataValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordVerificationViewModel @Inject constructor(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PasswordVerificationState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<PasswordVerificationEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        validateEmail()
        validateCode()
    }

    fun onAction(action: PasswordVerificationAction) {
        when (action) {
            PasswordVerificationAction.OnGetCodeClicked -> {
                getVerificationCode()
            }

            PasswordVerificationAction.OnNextClicked -> {
                verifyCode()
            }
            else -> Unit
        }
    }

    private fun getVerificationCode() {
        _state.update {
            it.copy(
                codeSendState = CodeSendState.Loading
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            authRepository.requestResetCode(
                RequestCode(
                    email = _state.value.email.text.toString()
                )
            ).fold(
                onSuccess = {
                    showSnackBar(
                        SnackBarData(
                            text = UiText.StringResource(R.string.snackbar_send_code_success),
                        )
                    )
                    _state.update {
                        it.copy(
                            codeSendState = CodeSendState.Cooldown,
                            codeErrorMessage = UiText.StringResource(R.string.error_verification_code_blank)
                        )
                    }
                    startRequestCodeCountDown()
                    startVerifyCountdown()
                },
                onFailure = { exception ->
                    when (exception) {
                        is ApiException -> {
                            if (exception.errorCode == 122) {
                                showWithAlreadySnsDialog()
                            } else {
                                _state.update {
                                    it.copy(
                                        emailErrorMessage = UiText.DynamicString(exception.errorValue)
                                    )
                                }
                            }
                        }
                        else -> {
                            showSnackBar(
                                SnackBarData(
                                    text = UiText.StringResource(R.string.snackbar_http_500_error),
                                )
                            )
                        }
                    }
                    _state.update {
                        it.copy(
                            codeSendState = CodeSendState.Ready
                        )
                    }
                }
            )
        }
    }
    private var requestCodeTimerJob: Job? = null
    private fun startRequestCodeCountDown(seconds: Int = 30) {
        requestCodeTimerJob?.cancel()

        requestCodeTimerJob = viewModelScope.launch(Dispatchers.Main) {
            for (i in seconds downTo 1) {
                _state.update {
                    it.copy(
                        resendCooldownSeconds = i
                    )
                }
                delay(1000)
            }
            _state.update {
                it.copy(
                    codeSendState = CodeSendState.Ready,
                    resendCooldownSeconds = 0
                )
            }
        }
    }

    private var verifyTimerJob: Job? = null
    private fun startVerifyCountdown(seconds: Int = 180) {
        verifyTimerJob?.cancel()

        verifyTimerJob = viewModelScope.launch(Dispatchers.Main) {
            for (i in seconds downTo 1) {
                _state.update {
                    it.copy(
                        codeExpirationSeconds = i
                    )
                }
                delay(1000)
            }
            _state.update {
                it.copy(
                    codeErrorMessage = UiText.StringResource(R.string.error_verification_code_over),
                    codeExpirationSeconds = 0,
                    isNextEnabled = false
                )
            }
        }
    }

    private fun verifyCode() {
        _state.update {
            it.copy(
                isVerifyingCode = true
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            authRepository.verifyResetCode(
                VerifyCode(
                    email = _state.value.email.text.toString(),
                    code = _state.value.code.text.toString()
                )
            ).fold(
                onSuccess = {
                    requestCodeTimerJob?.cancel()
                    verifyTimerJob?.cancel()
                    _eventChannel.send(PasswordVerificationEvent.VerificationSuccess(_state.value.email.text.toString()))
                },
                onFailure = { exception ->
                    when (exception) {
                        is ApiException -> {
                            _state.update {
                                it.copy(
                                    codeErrorMessage = UiText.DynamicString(exception.errorValue)
                                )
                            }
                        }
                        else -> {
                            showSnackBar(
                                SnackBarData(
                                    text = UiText.StringResource(R.string.snackbar_http_500_error),
                                )
                            )
                        }
                    }
                }
            )
            _state.update {
                it.copy(
                    isVerifyingCode = false
                )
            }
        }
    }

    private fun validateEmail() {
        viewModelScope.launch(Dispatchers.Main) {
            snapshotFlow { _state.value.email.text.toString() }
                .collectLatest { email ->
                    val isValid = userDataValidator.isValidEmail(email)

                    _state.update {
                        it.copy(
                            isEmailValid = isValid,
                            emailErrorMessage = when {
                                !isValid.isBlank -> null
                                !isValid.matchesEmailPattern -> {
                                    UiText.StringResource(R.string.error_email_invalid_format)
                                }
                                !isValid.hasMaxLength -> {
                                    UiText.StringResource(R.string.error_email_too_long)
                                }
                                else -> null
                            },
                            isNextEnabled = isValid.isValidEmail && it.code.text.isNotBlank() && it.codeExpirationSeconds > 0 && !it.isVerifyingCode
                        )
                    }
                }
        }
    }

    private fun validateCode() {
        viewModelScope.launch(Dispatchers.Main) {
            snapshotFlow { _state.value.code.text.toString() }
                .collectLatest { code ->
                    _state.update {
                        it.copy(
                            isNextEnabled = it.isEmailValid.isValidEmail && code.isNotBlank() && it.codeExpirationSeconds > 0 && !it.isVerifyingCode
                        )
                    }
                }
        }
    }

    private fun showWithAlreadySnsDialog() {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                PasswordVerificationEvent.VerificationWithDialog(
                    dialogData = DialogData(
                        type = DialogType.CONFIRM,
                        title = UiText.StringResource(R.string.dialog_already_sns_title),
                        subTitle = UiText.StringResource(R.string.dialog_already_sns_subtitle),
                        dismiss = UiText.StringResource(R.string.dialog_dismiss),
                        confirm = UiText.StringResource(R.string.dialog_already_sns_confirm),
                    )
                )
            )
        }
    }

    private fun showSnackBar(snackBarData: SnackBarData) {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                PasswordVerificationEvent.VerificationWithSnackBar(
                    snackBarData = snackBarData,
                )
            )
        }
    }
}