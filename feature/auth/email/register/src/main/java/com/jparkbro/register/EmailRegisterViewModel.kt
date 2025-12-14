package com.jparkbro.register

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.domain.EmailRegisterUseCase
import com.jparkbro.model.exception.ApiException
import com.jparkbro.ui.R
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
class EmailRegisterViewModel @Inject constructor(
    private val userDataValidator: UserDataValidator,
    private val emailRegisterUseCase: EmailRegisterUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(EmailRegisterState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<EmailRegisterEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        validateEmail()
        validatePassword()
    }

    fun onAction(action: EmailRegisterAction) {
        when (action) {
            EmailRegisterAction.OnTogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            EmailRegisterAction.OnAllAgreeClicked -> {
                _state.update {
                    it.copy(
                        isAllAgreed = !it.isAllAgreed,
                        isAgeVerified = !it.isAllAgreed,
                        isTermsOfServiceAccepted = !it.isAllAgreed,
                        isPrivacyPolicyAccepted = !it.isAllAgreed,
                        isRegisterEnabled = it.isEmailValid.isValidEmail && it.isPasswordValid.isValidPassword && !it.isRegisterIng && !it.isAllAgreed
                    )
                }
            }
            EmailRegisterAction.OnAgeVerificationClicked -> {
                _state.update {
                    it.copy(
                        isAgeVerified = !it.isAgeVerified,
                        isAllAgreed = !it.isAgeVerified && it.isTermsOfServiceAccepted && it.isPrivacyPolicyAccepted,
                        isRegisterEnabled = it.isEmailValid.isValidEmail && it.isPasswordValid.isValidPassword && !it.isRegisterIng && !it.isAgeVerified && it.isTermsOfServiceAccepted && it.isPrivacyPolicyAccepted
                    )
                }
            }
            EmailRegisterAction.OnTermsOfServiceClicked -> {
                _state.update {
                    it.copy(
                        isTermsOfServiceAccepted = !it.isTermsOfServiceAccepted,
                        isAllAgreed = it.isAgeVerified && !it.isTermsOfServiceAccepted && it.isPrivacyPolicyAccepted,
                        isRegisterEnabled = it.isEmailValid.isValidEmail && it.isPasswordValid.isValidPassword && !it.isRegisterIng && it.isAgeVerified && !it.isTermsOfServiceAccepted && it.isPrivacyPolicyAccepted
                    )
                }
            }
            EmailRegisterAction.OnPrivacyPolicyClicked -> {
                _state.update {
                    it.copy(
                        isPrivacyPolicyAccepted = !it.isPrivacyPolicyAccepted,
                        isAllAgreed = it.isAgeVerified && it.isTermsOfServiceAccepted && !it.isPrivacyPolicyAccepted,
                        isRegisterEnabled = it.isEmailValid.isValidEmail && it.isPasswordValid.isValidPassword && !it.isRegisterIng && it.isAgeVerified && it.isTermsOfServiceAccepted && !it.isPrivacyPolicyAccepted
                    )
                }
            }
            EmailRegisterAction.OnRegisterClicked -> {
                register()
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
                            isRegisterEnabled = isValid.isValidEmail && it.isPasswordValid.isValidPassword && !it.isRegisterIng && it.isAllAgreed
                        )
                    }
                }
        }
    }

    private fun validatePassword() {
        viewModelScope.launch(Dispatchers.Main) {
            snapshotFlow { _state.value.password.text.toString() }
                .collectLatest { password ->
                    val isValid = userDataValidator.isValidPassword(password)

                    _state.update {
                        it.copy(
                            isPasswordValid = isValid,
                            isRegisterEnabled = it.isEmailValid.isValidEmail && isValid.isValidPassword && !it.isRegisterIng && it.isAllAgreed
                        )
                    }
                }
        }
    }

    private fun register() {
        _state.update {
            it.copy(
                isRegisterIng = true,
                isRegisterEnabled = false
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            emailRegisterUseCase(
                email = _state.value.email.text.toString(),
                password = _state.value.password.text.toString(),
                termsAndConditions = _state.value.isAllAgreed
            ).collect { result ->
                result.fold(
                    onSuccess = {
                        _eventChannel.send(EmailRegisterEvent.RegisterSuccess)
                    },
                    onFailure = { exception ->
                        when (exception) {
                            is ApiException -> {
                                if (exception.errorCode == 109) {
                                    _state.update {
                                        it.copy(
                                            emailErrorMessage = UiText.DynamicString(exception.errorValue)
                                        )
                                    }
                                } else {
                                    showSnackBar(
                                        SnackBarData(
                                            text = UiText.DynamicString(exception.errorValue),
                                        )
                                    )
                                }
                            }
                            else -> {
                                showSnackBar(
                                    SnackBarData(
                                        text = UiText.StringResource(R.string.email_register_failed),
                                    )
                                )
                            }
                        }
                    }
                )
            }

            _state.update {
                it.copy(
                    isRegisterIng = false,
                    isRegisterEnabled = it.isEmailValid.isValidEmail && it.isPasswordValid.isValidPassword && it.isAllAgreed
                )
            }
        }
    }

    private fun showSnackBar(snackBarData: SnackBarData) {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                EmailRegisterEvent.RegisterFailWithSnackBar(
                    snackBarData = snackBarData,
                )
            )
        }
    }
}