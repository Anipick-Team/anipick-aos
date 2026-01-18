package com.jparkbro.model.dto.mypage.useredit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateEmailRequest(
    @SerialName("newEmail")
    val newEmail: String,
    @SerialName("password")
    val password: String,
)
