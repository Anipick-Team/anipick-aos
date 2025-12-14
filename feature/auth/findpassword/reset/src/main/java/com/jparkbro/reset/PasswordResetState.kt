package com.jparkbro.reset

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.ui.util.UiText
import com.jparkbro.util.PasswordValidationState

data class PasswordResetState(
    val password: TextFieldState = TextFieldState(),
    val isPasswordValid: PasswordValidationState = PasswordValidationState(),
    val isPasswordVisible: Boolean = false,
    val confirmPassword: TextFieldState = TextFieldState(),
    val confirmPasswordErrorMessage: UiText? = null,
    val isConfirmPasswordVisible: Boolean = false,
    val isChangeIng: Boolean = false,
    val isChangeEnabled: Boolean = false,
)
