package com.jparkbro.model.dto.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveMyReviewRequest(
    @SerialName("content")
    val content: String,
    @SerialName("rating")
    val rating: Float,
    @SerialName("isSpoiler")
    val isSpoiler: Boolean,
)
