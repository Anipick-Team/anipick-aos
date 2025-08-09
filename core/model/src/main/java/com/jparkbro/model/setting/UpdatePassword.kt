package com.jparkbro.model.setting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePassword(
    @SerialName("currentPassword")
    val currentPassword: String,
    @SerialName("newPassword")
    val newPassword: String,
    @SerialName("confirmNewPassword")
    val confirmNewPassword: String,
)
