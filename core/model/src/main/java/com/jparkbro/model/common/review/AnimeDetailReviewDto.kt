package com.jparkbro.model.common.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDetailReviewDto(
    @SerialName("userId")
    val userId: Int?,
    @SerialName("reviewId")
    val reviewId: Int?,
    @SerialName("nickname")
    val nickname: String?,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    @SerialName("rating")
    val rating: Float?,
    @SerialName("content")
    val content: String?,
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("isSpoiler")
    val isSpoiler: Boolean?,
    @SerialName("likeCount")
    val likeCount: Int?,
    @SerialName("isLiked")
    val isLiked: Boolean?,
    @SerialName("isMine")
    val isMine: Boolean?,
)

fun AnimeDetailReviewDto.toReview() : Review = Review(
    userId = userId ?: 0,
    reviewId = reviewId ?: 0,
    nickname = nickname,
    profileImageUrl = profileImageUrl,
    rating = rating ?: 0f,
    content = content,
    createdAt = createdAt,
    isSpoiler = isSpoiler ?: false,
    likeCount = likeCount ?: 0,
    isLiked = isLiked ?: false,
    isMine = isMine ?: false
)
