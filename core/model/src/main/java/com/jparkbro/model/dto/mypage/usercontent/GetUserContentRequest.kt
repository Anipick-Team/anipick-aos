package com.jparkbro.model.dto.mypage.usercontent

import com.jparkbro.model.enum.ReviewSortType
import com.jparkbro.model.enum.UserContentType
import kotlinx.serialization.Serializable

@Serializable
data class GetUserContentRequest(
    val contentType: UserContentType? = null,
    val lastId: Long? = null,
    val lastLikeCount: String? = null,
    val lastRating: String? = null,
    val sort: String = ReviewSortType.LATEST.param,
    val reviewOnly: Boolean = false,
    val size: Int? = null,
)
