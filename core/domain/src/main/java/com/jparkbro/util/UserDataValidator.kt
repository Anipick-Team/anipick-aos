package com.jparkbro.util

import android.util.Patterns
import javax.inject.Inject

class UserDataValidator @Inject constructor() {

    fun isValidEmail(email: String): EmailValidationState {
        val matchesEmailPattern = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val hasMaxLength = email.length <= MAX_EMAIL_LENGTH
        val isBlank = email.isNotBlank()

        return EmailValidationState(
            isBlank = isBlank,
            matchesEmailPattern = matchesEmailPattern,
            hasMaxLength = hasMaxLength
        )
    }

    fun isValidPassword(password: String): PasswordValidationState {
        val hasMinLength = password.length >= MIN_PASSWORD_LENGTH
        val hasMaxLength = password.length <= MAX_PASSWORD_LENGTH
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        return PasswordValidationState(
            hasMinLength = hasMinLength,
            hasMaxLength = hasMaxLength,
            hasDigit = hasDigit,
            hasSpecialChar = hasSpecialChar
        )
    }

    companion object {
        const val MAX_EMAIL_LENGTH = 50
        const val MIN_PASSWORD_LENGTH = 8
        const val MAX_PASSWORD_LENGTH = 16
    }
}