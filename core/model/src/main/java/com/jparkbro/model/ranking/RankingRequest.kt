package com.jparkbro.model.ranking

import com.jparkbro.model.common.ResponseMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RankingRequest(
    val type: RankingType,
    @SerialName("year")
    val year: String? = null,
    @SerialName("season")
    val season: String? = null,
    @SerialName("genre")
    val genre: ResponseMap? = null,
    @SerialName("lastId")
    val lastId: Int? = null,
    @SerialName("lastValue")
    val lastValue: Long? = null,
    @SerialName("lastRank")
    val lastRank: Long? = null,
    @SerialName("size")
    val size: Int? = null,
)
