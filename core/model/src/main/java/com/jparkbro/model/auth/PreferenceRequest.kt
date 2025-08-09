package com.jparkbro.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class PreferenceRequest(
    val query: String? = null,
    val year: String? = null,
    val season: Int? = null,
    val genres: Int? = null,
    val lastId: Int? = null,
    val size: Int? = null,
)

@Serializable
data class RatedAnime(
    @SerialName("animeId")
    val animeId: Int,
    @SerialName("rating")
    val rating: Float,
)