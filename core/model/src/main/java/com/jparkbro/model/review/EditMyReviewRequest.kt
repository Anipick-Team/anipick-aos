package com.jparkbro.model.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditMyReviewRequest(
    @SerialName("content")
    val content: String,
    @SerialName("rating")
    val rating: Float,
    @SerialName("isSpoiler")
    val isSpoiler: Boolean,
)
