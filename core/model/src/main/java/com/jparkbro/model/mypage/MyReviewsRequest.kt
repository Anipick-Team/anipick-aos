package com.jparkbro.model.mypage

import com.jparkbro.model.detail.ReviewSort
import kotlinx.serialization.Serializable

@Serializable
data class MyReviewsRequest(
    val lastId: Int? = null,
    val lastLikeCount: String? = null,
    val lastRating: String? = null,
    val sort: String = ReviewSort.LATEST.param,
    val reviewOnly: Boolean = false,
    val size: Int? = null,
)