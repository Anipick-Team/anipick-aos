package com.jparkbro.model.dto.preference

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatedAnime(
    @SerialName("animeId")
    val animeId: Long,
    @SerialName("rating")
    val rating: Float,
)