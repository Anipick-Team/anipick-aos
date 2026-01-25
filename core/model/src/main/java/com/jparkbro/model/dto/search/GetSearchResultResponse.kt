package com.jparkbro.model.dto.search

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.Studio
import com.jparkbro.model.common.actor.SearchPersonDto
import com.jparkbro.model.common.actor.toPerson
import com.jparkbro.model.common.anime.SearchAnimeDto
import com.jparkbro.model.common.anime.toAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSearchResultResponse(
    @SerialName("count")
    val count: Int,
    @SerialName("animeCount")
    val animeCount: Int? = null,
    @SerialName("personCount")
    val personCount: Int? = null,
    @SerialName("studioCount")
    val studioCount: Int? = null,
    @SerialName("nextPage")
    val nextPage: Int? = null,
    @SerialName("cursor")
    val cursor: Cursor? = null,
    @SerialName("animes")
    val animes: List<SearchAnimeDto> = emptyList(),
    @SerialName("persons")
    val persons: List<SearchPersonDto> = emptyList(),
    @SerialName("studios")
    val studios: List<Studio> = emptyList(),
)

fun GetSearchResultResponse.toResult() : GetSearchResultResult = GetSearchResultResult(
    count = count,
    animeCount = animeCount,
    personCount = personCount,
    studioCount = studioCount,
    nextPage = nextPage,
    cursor = cursor,
    animes = animes.map { it.toAnime() },
    persons = persons.map { it.toPerson() },
    studios = studios
)