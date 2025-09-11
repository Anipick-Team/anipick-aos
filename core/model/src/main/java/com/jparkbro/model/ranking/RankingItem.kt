package com.jparkbro.model.ranking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RankingItem(
    @SerialName("animeId")
    val animeId: Int,
    @SerialName("title")
    val title: String?,
    @SerialName("coverImageUrl")
    val coverImageUrl: String,
    @SerialName("rank")
    val rank: Int,
    @SerialName("change")
    val change: String? = null, // 'N' : "NEW"
    @SerialName("trend")
    val trend: RankingTrend? = null,
    @SerialName("genres")
    val genres: List<String>,
    @SerialName("popularity")
    val popularity: Int,
    @SerialName("trending")
    val trending: Int? = null,
)

@Serializable
enum class RankingTrend {
    @SerialName("up")
    UP,

    @SerialName("down")
    DOWN,

    @SerialName("same")
    SAME,
}