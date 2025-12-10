package com.jparkbro.util

data class EmailValidationState(
    val isBlank: Boolean = false,
    val matchesEmailPattern: Boolean = false,
    val hasMaxLength: Boolean = false,
    val isAlreadyRegistered: Boolean = false
) {
    val isValidEmail: Boolean
        get() = isBlank && matchesEmailPattern && hasMaxLength && !isAlreadyRegistered
}
