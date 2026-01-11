package com.jparkbro.model.common.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudioAnimeDto(
    @SerialName("animeId")
    val animeId: Long? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
    @SerialName("seasonYear")
    val seasonYear: String? = null,
)

fun StudioAnimeDto.toAnime() : Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    seasonYear = seasonYear ?: ""
)