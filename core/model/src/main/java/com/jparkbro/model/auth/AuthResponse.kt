package com.jparkbro.model.auth

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

/**
 * @property accessToken: 유효시간 1시간, Auto Login 시 사용
 * @property refreshToken: 유효기간 14일, accessToken 재발급 시 사용, 만료시 Logout
 */
@Serializable
data class AuthToken(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
)
