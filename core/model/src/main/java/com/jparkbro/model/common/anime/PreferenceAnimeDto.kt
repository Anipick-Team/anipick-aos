package com.jparkbro.model.common.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PreferenceAnimeDto(
    @SerialName("animeId")
    val animeId: Long? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
    @SerialName("genres")
    val genres: List<String?> = emptyList()
)

fun PreferenceAnimeDto.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    genres = genres
)