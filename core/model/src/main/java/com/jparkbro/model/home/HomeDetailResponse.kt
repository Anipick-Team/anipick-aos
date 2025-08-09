package com.jparkbro.model.home

import com.jparkbro.model.common.Cursor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeDetailResponse(
    @SerialName("count")
    val count: Int? = null,
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("referenceAnimeTitle")
    val referenceAnimeTitle: String? = null,
    @SerialName("animes")
    val animes: List<ComingSoonItem> = emptyList(),
    @SerialName("reviews")
    val reviews: List<HomeReviewItem> = emptyList(),
)