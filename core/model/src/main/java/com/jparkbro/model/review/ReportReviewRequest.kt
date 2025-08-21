package com.jparkbro.model.review

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportReviewRequest(
    @SerialName("message")
    val message: String,
)
