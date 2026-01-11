package com.jparkbro.model.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailRegisterRequest(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("termsAndConditions")
    val termsAndConditions: Boolean = false,
)
