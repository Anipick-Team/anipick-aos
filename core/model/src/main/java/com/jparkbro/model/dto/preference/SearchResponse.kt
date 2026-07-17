package com.jparkbro.model.dto.preference

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.anime.PreferenceAnimeDto
import com.jparkbro.model.common.anime.toAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("count")
    val count: Int? = null,
    @SerialName("cursor")
    val cursor: Cursor? = null,
    @SerialName("animes")
    val animes: List<PreferenceAnimeDto> = emptyList()
)

fun SearchResponse.toResult(): SearchResult = SearchResult(
    count = count,
    cursor = cursor,
    animes = animes.map { it.toAnime() }
)
