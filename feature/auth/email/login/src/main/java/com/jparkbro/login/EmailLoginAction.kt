package com.jparkbro.login

sealed interface EmailLoginAction {
    data object OnBackClick : EmailLoginAction
    data object OnTogglePasswordVisibility : EmailLoginAction
    data object OnEmailRegisterClick : EmailLoginAction
    data object OnFindPasswordClick : EmailLoginAction
    data object OnLoginClick : EmailLoginAction
}