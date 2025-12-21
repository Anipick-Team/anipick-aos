package com.jparkbro.model.common.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreferenceAnimeDto(
    @SerialName("animeId")
    val animeId: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
    @SerialName("genres")
    val genres: List<String?> = emptyList()
)

fun PreferenceAnimeDto.toAnime(): Anime = Anime(
    animeId = animeId ?: -1,
    title = title ?: "애니메이션 제목",
    coverImageUrl = coverImageUrl,
    genres = genres
)