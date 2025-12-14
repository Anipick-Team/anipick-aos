package com.jparkbro.login

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.domain.EmailLoginUseCase
import com.jparkbro.model.enum.DialogType
import com.jparkbro.model.exception.ApiException
import com.jparkbro.ui.R
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.util.UiText
import com.jparkbro.util.UserDataValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailLoginViewModel @Inject constructor(
    private val userDataValidator: UserDataValidator,
    private val emailLoginUseCase: EmailLoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EmailLoginState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<EmailLoginEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        validateEmail()
        validatePassword()
    }

    fun onAction(action: EmailLoginAction) {
        when (action) {
            EmailLoginAction.OnLoginClicked -> {
                login()
            }

            EmailLoginAction.OnTogglePasswordVisibility -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }

            else -> Unit
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
                            isLoginEnabled = isValid.isValidEmail && it.isPasswordValid && !it.isLoggingIn
                        )
                    }
                }
        }
    }

    private fun validatePassword() {
        viewModelScope.launch(Dispatchers.Main) {
            snapshotFlow { _state.value.password.text.toString() }
                .collectLatest { password ->
                    val isValid = password.isNotBlank()

                    _state.update {
                        it.copy(
                            isPasswordValid = isValid,
                            isLoginEnabled = it.isEmailValid.isValidEmail && isValid && !it.isLoggingIn
                        )
                    }
                }
        }
    }

    private fun login() {
        _state.update {
            it.copy(
                isLoggingIn = true,
                isLoginEnabled = false
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            emailLoginUseCase(
                email = _state.value.email.text.toString(),
                password = _state.value.password.text.toString()
            ).collect { result ->
                result.fold(
                    onSuccess = { reviewCompletedYn ->
                        _eventChannel.send(EmailLoginEvent.LoginSuccess(reviewCompletedYn))
                    },
                    onFailure = { exception ->
                        if (exception is ApiException) {
                            when (exception.errorCode) {
                                132 -> {
                                    showWithdrawnAccountDialog()
                                }
                                else -> {
                                    _state.update {
                                        it.copy(
                                            loginErrorMessage = exception.errorValue
                                        )
                                    }
                                }
                            }
                        } else {
                            showSnackBar(
                                SnackBarData(
                                    text = UiText.StringResource(R.string.snackbar_login_failed),
                                )
                            )
                        }
                    }
                )
            }

            _state.update {
                it.copy(
                    isLoggingIn = false,
                    isLoginEnabled = it.isEmailValid.isValidEmail && it.isPasswordValid
                )
            }
        }
    }

    private fun showWithdrawnAccountDialog() {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                EmailLoginEvent.LoginFailWithDialog(
                    dialogData = DialogData(
                        type = DialogType.ALERT,
                        title = UiText.StringResource(R.string.dialog_account_withdrawn),
                        subTitle = UiText.StringResource(R.string.dialog_account_withdrawn_message),
                        confirm = UiText.StringResource(R.string.dialog_dismiss),
                    )
                )
            )
        }
    }

    private fun showSnackBar(snackBarData: SnackBarData) {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                EmailLoginEvent.LoginFailWithSnackBar(
                    snackBarData = snackBarData,
                )
            )
        }
    }
}