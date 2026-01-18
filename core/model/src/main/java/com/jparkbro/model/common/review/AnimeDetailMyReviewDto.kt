package com.jparkbro.model.common.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDetailMyReviewDto(
    @SerialName("reviewId")
    val reviewId: Long? = null,
    @SerialName("rating")
    val rating: Float? = null,
    @SerialName("content")
    val content: String? = null,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("likeCount")
    val likeCount: Int? = null,
    @SerialName("isLiked")
    val isLiked: Boolean? = null,
)

fun AnimeDetailMyReviewDto.toReview() : Review = Review(
    reviewId = reviewId,
    rating = rating ?: 0f,
    content = content,
    createdAt = createdAt,
    likeCount = likeCount,
    isLiked = isLiked ?: false,
)
