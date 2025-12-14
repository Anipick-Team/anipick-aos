package com.jparkbro.util

data class PasswordValidationState(
    val hasMinLength: Boolean = false,
    val hasMaxLength: Boolean = false,
    val hasDigit: Boolean = false,
    val hasSpecialChar: Boolean = false
) {
    val isValidPassword: Boolean
        get() = hasMinLength && hasMaxLength && hasDigit && hasSpecialChar
}