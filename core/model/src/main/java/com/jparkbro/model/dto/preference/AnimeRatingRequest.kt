package com.jparkbro.model.dto.preference

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class AnimeRatingRequest(
    val animes: List<RatedAnime> = emptyList()
)

@Serializable
data class RatedAnime(
    @SerialName("animeId")
    val animeId: Int,
    @SerialName("rating")
    val rating: Float,
)