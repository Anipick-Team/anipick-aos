package com.jparkbro.model.common.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeDetailReviewDto(
    @SerialName("reviewId")
    val reviewId: Long?,
    @SerialName("userId")
    val userId: Long?,
    @SerialName("animeId")
    val animeId: Long?,
    @SerialName("animeTitle")
    val animeTitle: String?,
    @SerialName("animeCoverImageUrl")
    val animeCoverImageUrl: String?,
    @SerialName("rating")
    val rating: Float?,
    @SerialName("reviewContent")
    val content: String?,
    @SerialName("nickname")
    val nickname: String?,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("likeCount")
    val likeCount: Int?,
    @SerialName("likedByCurrentUser")
    val likedByCurrentUser: Boolean?,
    @SerialName("isMine")
    val isMine: Boolean?,
)

fun HomeDetailReviewDto.toReview(): Review = Review(
    reviewId = reviewId,
    userId = userId,
    animeId = animeId,
    animeTitle = animeTitle,
    animeCoverImageUrl = animeCoverImageUrl,
    rating = rating,
    content = content,
    nickname = nickname,
    profileImageUrl = profileImageUrl,
    createdAt = createdAt,
    likeCount = likeCount,
    likedByCurrentUser = likedByCurrentUser ?: false,
    isMine = isMine ?: false
)