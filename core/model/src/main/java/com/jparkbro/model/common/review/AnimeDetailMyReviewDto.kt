package com.jparkbro.model.common.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDetailMyReviewDto(
    @SerialName("reviewId")
    val reviewId: Int?,
    @SerialName("rating")
    val rating: Float?,
    @SerialName("content")
    val content: String?,
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("likeCount")
    val likeCount: Int?,
)

fun AnimeDetailMyReviewDto.toReview() : Review = Review(
    reviewId = reviewId,
    rating = rating ?: 0f,
    content = content,
    createdAt = createdAt,
    likeCount = likeCount
)
