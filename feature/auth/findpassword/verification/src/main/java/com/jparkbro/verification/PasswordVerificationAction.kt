package com.jparkbro.verification

interface PasswordVerificationAction {
    data object OnBackClicked : PasswordVerificationAction
    data object OnGetCodeClicked : PasswordVerificationAction
    data object OnNextClicked: PasswordVerificationAction
}