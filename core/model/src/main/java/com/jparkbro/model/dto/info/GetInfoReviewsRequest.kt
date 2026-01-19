package com.jparkbro.model.dto.info

import com.jparkbro.model.enum.ReviewSortType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetInfoReviewsRequest(
    @SerialName("animeId")
    val animeId: Long,
    @SerialName("sort")
    val sort: String = ReviewSortType.LATEST.param,
    @SerialName("isSpoiler")
    val isSpoiler: Boolean = true,
    @SerialName("lastValue")
    val lastValue: String? = null,
    @SerialName("lastId")
    val lastId: Long? = null,
    @SerialName("size")
    val size: Int? = 20,
)