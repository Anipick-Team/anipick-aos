package com.jparkbro.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
