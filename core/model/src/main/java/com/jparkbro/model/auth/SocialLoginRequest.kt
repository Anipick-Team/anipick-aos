package com.jparkbro.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialLoginRequest(
    val platform: String = "android",
    val code: String,
)

@Serializable
enum class LoginProvider(val value: String, val displayName: String) {
    @SerialName("LOCAL")
    Local("Local", ""),

    @SerialName("KAKAO")
    KAKAO("KAKAO", "카카오톡"),

    @SerialName("GOOGLE")
    GOOGLE("GOOGLE", "구글"),

    @SerialName("APPLE")
    APPLE("APPLE", "애플"),
}