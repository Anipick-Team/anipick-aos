package com.jparkbro.verification

interface PasswordVerificationAction {
    data object NavigateBack : PasswordVerificationAction
    data object OnGetCodeClicked : PasswordVerificationAction
    data object OnNextClicked: PasswordVerificationAction
}