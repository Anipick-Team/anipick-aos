package com.jparkbro.model.dto.mypage.setting

import com.jparkbro.model.auth.LoginProvider

data class UserResult(
    val nickname: String? = null,
    val email: String? = null,
    val provider: LoginProvider? = LoginProvider.LOCAL,
)
