package com.jparkbro.model.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyReview(
    @SerialName("reviewId")
    val reviewId: Int,
    @SerialName("animeId")
    val animeId: Int,
    @SerialName("rating")
    val rating: Float,
    @SerialName("content")
    val content: String? = null,
    @SerialName("isSpoiler")
    val isSpoiler: Boolean = false,
)
