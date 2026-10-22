package com.jparkbro.auth.impl.login

data class LoginState(
    /** 소셜 토큰을 받은 뒤 우리 서버 로그인 API를 기다리는 중 */
    val isLoggingIn: Boolean = false,
)
