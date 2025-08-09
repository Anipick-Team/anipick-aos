package com.jparkbro.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cursor(
    @SerialName("lastId")
    val lastId: Int?,
    @SerialName("sort")
    val sort: String? = null,
    @SerialName("lastValue")
    val lastValue: String? = null,
)