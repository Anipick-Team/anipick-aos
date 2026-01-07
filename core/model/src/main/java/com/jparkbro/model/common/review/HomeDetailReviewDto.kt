package com.jparkbro.model.common.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeDetailReviewDto(
    @SerialName("reviewId")
    val reviewId: Long? = null,
    @SerialName("userId")
    val userId: Long? = null,
    @SerialName("animeId")
    val animeId: Long? = null,
    @SerialName("animeTitle")
    val animeTitle: String? = null,
    @SerialName("animeCoverImageUrl")
    val animeCoverImageUrl: String? = null,
    @SerialName("rating")
    val rating: Float? = null,
    @SerialName("reviewContent")
    val content: String? = null,
    @SerialName("nickname")
    val nickname: String? = null,
    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("likeCount")
    val likeCount: Int? = null,
    @SerialName("likedByCurrentUser")
    val likedByCurrentUser: Boolean = false,
    @SerialName("isMine")
    val isMine: Boolean = false,
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
    likedByCurrentUser = likedByCurrentUser,
    isMine = isMine
)