package com.jparkbro.model.detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDetailRequest(
    @SerialName("animeId")
    val animeId: Long,
    @SerialName("sort")
    val sort: String = ReviewSort.LATEST.param,
    @SerialName("isSpoiler")
    val isSpoiler: Boolean = false,
    @SerialName("lastValue")
    val lastValue: String? = null,
    @SerialName("lastId")
    val lastId: Long? = null,
    @SerialName("size")
    val size: Int? = 20,
)

@Serializable
enum class ReviewSort(val param: String, val displayName: String) {
    LATEST("latest", "최신순"),
    LIKES("likes", "좋아요 순"),
    RATING_DESC("ratingDesc", "평가 높은 순"),
    RATING_ASC("ratingAsc", "평가 낮은 순")
}
