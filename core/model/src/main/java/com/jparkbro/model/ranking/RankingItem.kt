package com.jparkbro.model.ranking

import com.jparkbro.model.enum.RankingTrend
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RankingItem(
    @SerialName("animeId")
    val animeId: Long,
    @SerialName("title")
    val title: String?,
    @SerialName("coverImageUrl")
    val coverImageUrl: String,
    @SerialName("rank")
    val rank: Int,
    @SerialName("genres")
    val genres: List<String>,
    @SerialName("popularity")
    val popularity: Long,

    @SerialName("change")
    val change: String? = null, // 'N' : "NEW"
    @SerialName("trend")
    val trend: RankingTrend? = null,
    @SerialName("trending")
    val trending: Int? = null,
)

