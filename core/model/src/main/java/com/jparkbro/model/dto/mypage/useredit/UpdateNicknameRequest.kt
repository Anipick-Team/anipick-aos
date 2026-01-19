package com.jparkbro.model.dto.mypage.useredit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateNicknameRequest(
    @SerialName("nickname")
    val nickname: String,
)
