package com.jparkbro.login

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.ui.util.UiText
import com.jparkbro.util.EmailValidationState

data class EmailLoginState(
    val email: TextFieldState = TextFieldState(),
    val isEmailValid: EmailValidationState = EmailValidationState(),
    val emailErrorMessage: UiText? = null,
    val password: TextFieldState = TextFieldState(),
    val isPasswordValid: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isLoggingIn: Boolean = false,
    val loginErrorMessage: String? = null,
    val isLoginEnabled: Boolean = false,
)