package com.jparkbro.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppInitResponse(
    @SerialName("type")
    val type: AppInitDialogType,
    @SerialName("title")
    val title: String?,
    @SerialName("content")
    val content: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("isRequiredUpdate")
    val isRequiredUpdate: Boolean = false,
)

@Serializable
enum class AppInitDialogType {
    @SerialName("notice")
    NOTICE,
    @SerialName("update")
    UPDATE
}