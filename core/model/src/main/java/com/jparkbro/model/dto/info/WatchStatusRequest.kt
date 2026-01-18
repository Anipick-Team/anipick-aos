package com.jparkbro.model.dto.info

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchStatusRequest(
    @SerialName("status")
    val status: String,
)