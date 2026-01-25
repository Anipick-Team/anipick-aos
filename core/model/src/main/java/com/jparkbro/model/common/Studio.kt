package com.jparkbro.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Studio(
    @SerialName("studioId")
    val studioId: Long,
    @SerialName("name")
    val name: String? = null,
)
