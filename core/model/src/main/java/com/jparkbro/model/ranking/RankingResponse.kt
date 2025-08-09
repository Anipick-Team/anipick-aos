package com.jparkbro.model.ranking

import com.jparkbro.model.common.Cursor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RankingResponse(
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("animes")
    val animes: List<RankingItem>,
)

