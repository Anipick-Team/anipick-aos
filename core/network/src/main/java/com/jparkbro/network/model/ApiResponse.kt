package com.jparkbro.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    // 공통
    @SerialName("code")
    val code: Int,
    @SerialName("value")
    val value: String,

    // 성공시
    @SerialName("result")
    val result: T? = null,

    // 실패시
    @SerialName("errorReason")
    val errorReason: String = "",
    @SerialName("errorValue")
    val errorValue: String = "",
)
