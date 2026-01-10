package com.jparkbro.model.common.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InfoSeriesAnimeDto(
    @SerialName("animeId")
    val animeId: Long? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
    @SerialName("airDate")
    val airDate: String? = null,
)

fun InfoSeriesAnimeDto.toAnime() : Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    airDate = airDate
)