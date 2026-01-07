package com.jparkbro.model.dto.info

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.InfoSeriesAnimeDto
import com.jparkbro.model.common.anime.toAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetInfoSeriesResponse(
    @SerialName("count")
    val count: Int?,
    @SerialName("cursor")
    val cursor: Cursor?,
    @SerialName("animes")
    val animes: List<InfoSeriesAnimeDto>
)

fun GetInfoSeriesResponse.toResult() : GetInfoSeriesResult = GetInfoSeriesResult(
    count = count ?: 0,
    cursor = cursor ?: Cursor(),
    animes = animes.map { it.toAnime() }
)