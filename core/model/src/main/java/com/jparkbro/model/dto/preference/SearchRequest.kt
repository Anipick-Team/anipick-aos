package com.jparkbro.model.dto.preference

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRequest(
    @SerialName("query")
    val query: String? = null,
    @SerialName("year")
    val year: String? = null,
    @SerialName("season")
    val season: Int? = null,
    @SerialName("genres")
    val genres: Int? = null,
    @SerialName("lastId")
    val lastId: Long? = null,
    @SerialName("size")
    val size: Int? = null,
)