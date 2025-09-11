package com.jparkbro.model.detail

import com.jparkbro.model.common.Cursor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeSeriesResponse(
    @SerialName("count")
    val count: Int,
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("animes")
    val animes: List<DetailSeries>
)
