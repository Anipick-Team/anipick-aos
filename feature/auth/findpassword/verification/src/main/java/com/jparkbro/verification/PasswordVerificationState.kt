package com.jparkbro.verification

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.ui.util.UiText
import com.jparkbro.util.EmailValidationState

data class PasswordVerificationState(
    val email: TextFieldState = TextFieldState(),
    val isEmailValid: EmailValidationState = EmailValidationState(),
    val emailErrorMessage: UiText? = null,
    val code: TextFieldState = TextFieldState(),
    val codeErrorMessage: UiText? = null,
    val codeSendState: CodeSendState = CodeSendState.Idle,
    val resendCooldownSeconds: Int = 0,
    val codeExpirationSeconds: Int = 0,
    val isVerifyingCode: Boolean = false,
    val isNextEnabled: Boolean = false,
)

sealed interface CodeSendState {
    data object Idle: CodeSendState
    data object Loading: CodeSendState
    data object Cooldown : CodeSendState
    data object Ready : CodeSendState
}