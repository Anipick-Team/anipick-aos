package com.jparkbro.login

import android.app.Activity

sealed interface LoginAction {
    data class OnKakaoLoginClick(val activity: Activity) : LoginAction
    data class OnGoogleLoginClick(val activity: Activity) : LoginAction
    data object OnProblemClick : LoginAction
    data object NavigateToRegister : LoginAction
    data object NavigateToEmailLogin : LoginAction
}