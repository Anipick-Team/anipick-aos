package com.jparkbro.model.dto.info

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.review.AnimeDetailReviewDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetInfoReviewsResponse(
    @SerialName("count")
    val count: Int? = null,
    @SerialName("cursor")
    val cursor: Cursor? = null,
    @SerialName("reviews")
    val reviews: List<AnimeDetailReviewDto> = emptyList(),
)
