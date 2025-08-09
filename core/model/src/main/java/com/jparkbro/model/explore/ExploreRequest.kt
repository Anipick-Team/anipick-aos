package com.jparkbro.model.explore

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExploreRequest(
    @SerialName("year")
    val year: String?,
    @SerialName("season")
    val season: Int?,
    @SerialName("genres")
    val genres: List<Int>?,
    @SerialName("type")
    val type: String?,
    @SerialName("sort")
    val sort: String,
    @SerialName("lastId")
    val lastId: Int?,
    @SerialName("size")
    val size: Int = 18,
    @SerialName("genreOp")
    val genreOp: String = "OR",
    @SerialName("lastValue")
    val lastValue: String?,
)
