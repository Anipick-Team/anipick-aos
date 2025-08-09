package com.jparkbro.model.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewRating(
    @SerialName("rating")
    val rating: Float
)
