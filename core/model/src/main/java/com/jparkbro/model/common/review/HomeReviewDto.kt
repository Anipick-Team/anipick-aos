package com.jparkbro.model.common.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeReviewDto(
    @SerialName("reviewId")
    val reviewId: Long? = null,
    @SerialName("userId")
    val userId: Long? = null,
    @SerialName("animeId")
    val animeId: Long? = null,
    @SerialName("animeTitle")
    val animeTitle: String? = null,
    @SerialName("reviewContent")
    val content: String? = null,
    @SerialName("nickname")
    val nickname: String? = null,
    @SerialName("createdAt")
    val createdAt: String? = null,
)

fun HomeReviewDto.toReview(): Review = Review(
    reviewId = reviewId,
    userId = userId,
    animeId = animeId,
    animeTitle = animeTitle,
    content = content,
    nickname = nickname,
    createdAt = createdAt
)
