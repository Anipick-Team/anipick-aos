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
    val change: Int,
    @SerialName("trend")
    val trend: RankingTrend,
    @SerialName("genres")
    val genres: List<String>,
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