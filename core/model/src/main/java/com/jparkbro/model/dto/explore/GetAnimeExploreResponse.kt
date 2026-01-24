package com.jparkbro.model.dto.explore

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.SimpleAnimeDto
import com.jparkbro.model.common.anime.toAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetAnimeExploreResponse(
    @SerialName("count")
    val count: Int? = null,
    @SerialName("cursor")
    val cursor: Cursor? = null,
    @SerialName("animes")
    val animes: List<SimpleAnimeDto> = emptyList()
)

fun GetAnimeExploreResponse.toResult() : GetAnimeExploreResult = GetAnimeExploreResult(
    count = count,
    cursor = cursor,
    animes = animes.map { it.toAnime() }
)