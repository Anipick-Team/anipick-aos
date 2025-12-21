package com.jparkbro.model.dto.home.main

import com.jparkbro.model.common.anime.SimpleAnimeDto
import com.jparkbro.model.common.anime.toAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NextQuarterAnimesResponse(
    @SerialName("season")
    val season: Int? = null,
    @SerialName("seasonYear")
    val seasonYear: Int? = null,
    @SerialName("animes")
    val animes: List<SimpleAnimeDto> = emptyList(),
)

fun NextQuarterAnimesResponse.toResult(): NextQuarterAnimesResult = NextQuarterAnimesResult(
    season = season,
    seasonYear = seasonYear,
    animes = animes.map { it.toAnime() }
)