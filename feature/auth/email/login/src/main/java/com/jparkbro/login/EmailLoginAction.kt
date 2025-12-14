package com.jparkbro.login

sealed interface EmailLoginAction {
    data object OnBackClicked : EmailLoginAction
    data object OnTogglePasswordVisibility : EmailLoginAction
    data object OnEmailRegisterClicked : EmailLoginAction
    data object OnFindPasswordClicked : EmailLoginAction
    data object OnLoginClicked : EmailLoginAction
}