package com.jparkbro.model.setting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateEmail(
    @SerialName("newEmail")
    val newEmail: String,
    @SerialName("password")
    val password: String,
)
