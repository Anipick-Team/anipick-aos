package com.jparkbro.model.detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailMyReview(
    @SerialName("reviewId")
    val reviewId: Int?,
    @SerialName("rating")
    val rating: Float?,
    @SerialName("content")
    val content: String?,
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("likeCount")
    val likeCount: Int?,
)
