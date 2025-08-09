package com.jparkbro.model.setting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateNickname(
    @SerialName("nickname")
    val nickname: String,
)
