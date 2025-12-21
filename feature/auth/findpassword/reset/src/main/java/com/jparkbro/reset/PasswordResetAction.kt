package com.jparkbro.reset

interface PasswordResetAction {
    data object NavigateBack : PasswordResetAction
    data object OnTogglePasswordVisibility : PasswordResetAction
    data object OnTogglePasswordConfirmVisibility : PasswordResetAction
    data object OnCompleteClicked : PasswordResetAction
}