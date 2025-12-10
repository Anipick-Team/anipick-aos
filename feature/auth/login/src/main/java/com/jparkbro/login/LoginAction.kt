package com.jparkbro.login

import android.app.Activity

sealed interface LoginAction {
    data class OnKakaoLoginClick(val activity: Activity) : LoginAction
    data class OnGoogleLoginClick(val activity: Activity) : LoginAction
    data object OnEmailRegisterClick : LoginAction
    data object OnEmailLoginClick : LoginAction
    data object OnProblemClick : LoginAction
}