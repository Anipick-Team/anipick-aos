package com.jparkbro.model.dto.info

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.review.Review

data class GetInfoReviewsResult(
    val count: Int? = null,
    val cursor: Cursor? = null,
    val reviews: List<Review> = emptyList(),
)
