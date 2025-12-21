package com.jparkbro.model.common.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpcomingReleasesAnimeDto(
    @SerialName("animeId")
    val animeId: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
    @SerialName("releaseDate")
    val releaseDate: String? = null
)

fun UpcomingReleasesAnimeDto.toAnime(): Anime = Anime(
    animeId = animeId ?: -1,
    title = title ?: "애니메이션 제목",
    coverImageUrl = coverImageUrl,
    releaseDate = releaseDate
)