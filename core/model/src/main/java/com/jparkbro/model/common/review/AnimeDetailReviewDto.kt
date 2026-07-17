package com.jparkbro.model.common.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeDetailReviewDto(
    @SerialName("userId")
    val userId: Long?,
    @SerialName("reviewId")
    val reviewId: Long?,
    @SerialName("nickname")
    val nickname: String?,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    val profileImageByteArray: ByteArray? = null,
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
    userId = userId,
    reviewId = reviewId,
    nickname = nickname,
    profileImageUrl = profileImageUrl,
    profileImageByteArray = profileImageByteArray,
    rating = rating,
    content = content,
    createdAt = createdAt,
    isSpoiler = isSpoiler ?: false,
    likeCount = likeCount,
    isLiked = isLiked ?: false,
    isMine = isMine ?: false
)
