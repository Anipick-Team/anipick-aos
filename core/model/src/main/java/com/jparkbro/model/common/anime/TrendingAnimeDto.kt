package com.jparkbro.model.common.anime

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingAnimeDto(
    @SerialName("animeId")
    val animeId: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("titleKor")
    val titleKor: String? = null,
    @SerialName("titleEng")
    val titleEng: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
    @SerialName("rank")
    val rank: Int? = null,
)

fun TrendingAnimeDto.toAnime() : Anime = Anime(
    animeId = animeId ?: -1,
    title = title,
    titleKor = titleKor,
    titleEng = titleEng,
    coverImageUrl = coverImageUrl,
    rank = rank
)