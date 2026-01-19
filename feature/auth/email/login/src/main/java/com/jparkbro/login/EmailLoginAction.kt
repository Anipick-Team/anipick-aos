package com.jparkbro.login

sealed interface EmailLoginAction {
    data object NavigateBack : EmailLoginAction
    data object NavigateToRegister : EmailLoginAction
    data object NavigateToFindPassword : EmailLoginAction
    data object OnTogglePasswordVisibility : EmailLoginAction
    data object OnLoginClicked : EmailLoginAction
}