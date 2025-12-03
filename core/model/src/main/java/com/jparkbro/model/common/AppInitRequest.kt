package com.jparkbro.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppInitRequest(
    @SerialName("userAppVersion")
    val userAppVersion: String,
    @SerialName("platform")
    val platform: String = "ANDROID",
)
