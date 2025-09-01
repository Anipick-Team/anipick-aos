package com.jparkbro.model.detail

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.ReviewItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDetailResponse(
    @SerialName("count")
    val count: Int,
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("reviews")
    val reviews: List<DetailReviewItem>,
)

@Serializable
data class DetailReviewItem(
    @SerialName("userId")
    val userId: Int? = null,
    @SerialName("reviewId")
    override val reviewId: Int,
    @SerialName("nickname")
    override val nickname: String,
    @SerialName("profileImageUrl")
    override val profileImageUrl: String?,
    @SerialName("rating")
    override val rating: Float,
    @SerialName("content")
    override val content: String?,
    @SerialName("createdAt")
    override val createdAt: String,
    @SerialName("isSpoiler")
    val isSpoiler: Boolean,
    @SerialName("likeCount")
    override val likeCount: Int,
    @SerialName("isLiked")
    override val isLiked: Boolean = false,
    @SerialName("isMine")
    override val isMine: Boolean,
) : ReviewItem