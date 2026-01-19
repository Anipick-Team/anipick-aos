package com.jparkbro.model.auth

import com.jparkbro.model.common.AuthToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @property reviewCompletedYn
 *          - true: 기존 사용자 > Home Screen
 *          - false: 신규 사용자 > Preference Setup Screen
 */
@Serializable
data class AuthResponse(
    @SerialName("reviewCompletedYn")
    val reviewCompletedYn: Boolean = false,
    @SerialName("userId")
    val userId: Int = 0,
    @SerialName("nickname")
    val nickname: String = "",
    @SerialName("token")
    val token: AuthToken,
)
