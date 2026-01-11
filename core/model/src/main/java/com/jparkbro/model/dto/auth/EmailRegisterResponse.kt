package com.jparkbro.model.dto.auth

import com.jparkbro.model.common.AuthToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailRegisterResponse(
    @SerialName("reviewCompletedYn")
    val reviewCompletedYn: Boolean,
    @SerialName("userId")
    val userId: Long,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("token")
    val token: AuthToken,
)
