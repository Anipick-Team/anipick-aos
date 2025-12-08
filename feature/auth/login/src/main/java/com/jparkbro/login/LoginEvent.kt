package com.jparkbro.login

sealed interface LoginEvent {
    data class LoginSuccess(val reviewCompletedYn: Boolean) : LoginEvent
}