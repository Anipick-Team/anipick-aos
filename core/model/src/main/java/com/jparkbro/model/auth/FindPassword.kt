package com.jparkbro.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestCode(
    @SerialName("email")
    val email: String,
)

@Serializable
data class VerifyCode(
    @SerialName("email")
    val email: String,
    @SerialName("code")
    val code: String,
)

@Serializable
data class ResetPassword(
    @SerialName("email")
    val email: String,
    @SerialName("newPassword")
    val newPassword: String,
    @SerialName("checkNewPassword")
    val checkNewPassword: String,
)