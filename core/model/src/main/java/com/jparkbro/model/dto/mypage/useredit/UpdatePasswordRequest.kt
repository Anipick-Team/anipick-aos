package com.jparkbro.model.dto.mypage.useredit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePasswordRequest(
    @SerialName("currentPassword")
    val currentPassword: String,
    @SerialName("newPassword")
    val newPassword: String,
    @SerialName("confirmNewPassword")
    val confirmNewPassword: String,
)
