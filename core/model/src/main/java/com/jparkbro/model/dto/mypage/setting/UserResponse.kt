package com.jparkbro.model.dto.mypage.setting

import com.jparkbro.model.auth.LoginProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("nickname")
    val nickname: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("provider")
    val provider: LoginProvider? = LoginProvider.LOCAL,
)

fun UserResponse.toResult() : UserResult = UserResult(
    nickname = nickname,
    email = email,
    provider = provider,
)