package com.jparkbro.model.setting

import com.jparkbro.model.auth.LoginProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("email")
    val email: String,
    @SerialName("provider")
    val provider: LoginProvider = LoginProvider.Local,
)
