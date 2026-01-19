package com.jparkbro.model.dto.home.main

import com.jparkbro.model.common.anime.SimpleAnimeDto
import com.jparkbro.model.common.anime.toAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendedAnimesResponse(
    @SerialName("referenceAnimeTitle")
    val referenceAnimeTitle: String? = null,
    @SerialName("animes")
    val animes: List<SimpleAnimeDto> = emptyList(),
)

fun RecommendedAnimesResponse.toResult(): RecommendedAnimesResult = RecommendedAnimesResult(
    referenceAnimeTitle = referenceAnimeTitle,
    animes = animes.map { it.toAnime() }
)