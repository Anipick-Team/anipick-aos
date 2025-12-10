package com.jparkbro.register

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.ui.util.UiText
import com.jparkbro.util.EmailValidationState
import com.jparkbro.util.PasswordValidationState

data class EmailRegisterState(
    val email: TextFieldState = TextFieldState(),
    val isEmailValid: EmailValidationState = EmailValidationState(),
    val emailErrorMessage: UiText? = null,
    val password: TextFieldState = TextFieldState(),
    val isPasswordValid: PasswordValidationState = PasswordValidationState(),
    val isPasswordVisible: Boolean = false,
    val isAllAgreed: Boolean = false,
    val isAgeVerified: Boolean = false,
    val isTermsOfServiceAccepted: Boolean = false,
    val isPrivacyPolicyAccepted: Boolean = false,
    val isRegisterIng: Boolean = false,
    val canRegister: Boolean = false,
)
