package com.jparkbro.model.common.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleAnimeDto(
    @SerialName("animeId")
    val animeId: Long? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null
)

fun SimpleAnimeDto.toAnime(): Anime = Anime(
    animeId = animeId ?: -1,
    title = title ?: "애니메이션 제목",
    coverImageUrl = coverImageUrl
)