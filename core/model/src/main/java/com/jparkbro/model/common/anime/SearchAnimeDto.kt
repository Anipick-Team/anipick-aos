package com.jparkbro.model.common.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchAnimeDto(
    @SerialName("animeId")
    val animeId: Long,
    @SerialName("title")
    val title: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
    @SerialName("clickLog")
    val clickLog: String? = null,
    @SerialName("impressionLog")
    val impressionLog: String? = null,
)

fun SearchAnimeDto.toAnime(): Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    clickLog = clickLog,
    impressionLog = impressionLog
)