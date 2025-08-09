package com.jparkbro.model.explore

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.DefaultAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExploreResponse(
    @SerialName("count")
    val count: Int,
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("animes")
    val animes: List<DefaultAnime>
)
