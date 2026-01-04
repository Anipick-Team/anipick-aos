package com.jparkbro.model.common.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InfoSeriesAnimeDto(
    @SerialName("animeId")
    val animeId: Int?,
    @SerialName("title")
    val title: String?,
    @SerialName("coverImageUrl")
    val coverImageUrl: String?,
    @SerialName("airDate")
    val airDate: String?
)

fun InfoSeriesAnimeDto.toAnime() : Anime = Anime(
    animeId = animeId ?: -1,
    title = title,
    coverImageUrl = coverImageUrl,
    airDate = airDate
)