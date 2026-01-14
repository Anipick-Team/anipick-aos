package com.jparkbro.model.dto.info

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewRatingRequest(
    @SerialName("rating")
    val rating: Float
)
