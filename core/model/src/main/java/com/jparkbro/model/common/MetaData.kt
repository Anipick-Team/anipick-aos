package com.jparkbro.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaData(
    @SerialName("seasonYear")
    val seasonYear: List<String> = emptyList(),
    @SerialName("season")
    val season: List<ResponseMap> = emptyList(),
    @SerialName("genres")
    val genres: List<ResponseMap> = emptyList(),
    @SerialName("type")
    val type: List<String> = emptyList()
)

@Serializable
data class ResponseMap(
    @SerialName("id")
    val id: Int = -1,
    @SerialName("name")
    val name: String = "",
)

@Serializable
data class StudioMap(
    @SerialName("studioId")
    val studioId: Int,
    @SerialName("name")
    val name: String,
)