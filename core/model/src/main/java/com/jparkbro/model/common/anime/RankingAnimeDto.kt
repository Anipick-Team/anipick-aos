package com.jparkbro.model.common.anime

import com.jparkbro.model.enum.RankingTrend
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RankingAnimeDto(
    @SerialName("animeId")
    val animeId: Long,
    @SerialName("title")
    val title: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
    @SerialName("rank")
    val rank: Int? = null,
    @SerialName("change")
    val change: String? = null, // 'N' : "NEW"
    @SerialName("trend")
    val trend: RankingTrend? = null,
    @SerialName("genres")
    val genres: List<String> = emptyList(),
    @SerialName("popularity")
    val popularity: Int? = null,
    @SerialName("trending")
    val trending: Int? = null,
)

fun RankingAnimeDto.toAnime() : Anime = Anime(
    animeId = animeId,
    title = title,
    coverImageUrl = coverImageUrl,
    genres = genres,
    rank = rank,
    change = change,
    trend = trend,
    popularity = popularity,
    trending = trending,
)