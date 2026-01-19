package com.jparkbro.model.dto.info

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.InfoSeriesAnimeDto
import com.jparkbro.model.common.anime.toAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetInfoSeriesResponse(
    @SerialName("count")
    val count: Int? = null,
    @SerialName("cursor")
    val cursor: Cursor? = null,
    @SerialName("animes")
    val animes: List<InfoSeriesAnimeDto> = emptyList()
)

fun GetInfoSeriesResponse.toResult() : GetInfoSeriesResult = GetInfoSeriesResult(
    count = count,
    cursor = cursor,
    animes = animes.map { it.toAnime() }
)