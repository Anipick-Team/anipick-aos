package com.jparkbro.model.common.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserContentReviewDto(
    @SerialName("animeId")
    val animeId: Long? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,
    @SerialName("rating")
    val rating: Float = 0f,
    @SerialName("reviewId")
    val reviewId: Long? = null,
    @SerialName("reviewContent")
    val reviewContent: String? = null,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("likeCount")
    val likeCount: Int? = null,
    @SerialName("isLiked")
    val isLiked: Boolean = false,
)

fun UserContentReviewDto.toReview() : Review = Review(
    animeId = animeId,
    animeTitle = title,
    animeCoverImageUrl = coverImageUrl,
    rating = rating,
    reviewId = reviewId,
    content = reviewContent,
    createdAt = createdAt,
    likeCount = likeCount,
    isLiked = isLiked,
    isMine = true
)