package com.jparkbro.model.common.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewFormAnimeReviewDto(
    @SerialName("reviewId")
    val reviewId: Long? = null,
    @SerialName("animeId")
    val animeId: Long? = null,
    @SerialName("rating")
    val rating: Float = 0f,
    @SerialName("content")
    val content: String? = null,
    @SerialName("isSpoiler")
    val isSpoiler: Boolean = false,
)

fun ReviewFormAnimeReviewDto.toReview() : Review = Review(
    reviewId = reviewId ?: 0,
    animeId = animeId ?: 0,
    rating = rating,
    content = content,
    isSpoiler = isSpoiler,
)