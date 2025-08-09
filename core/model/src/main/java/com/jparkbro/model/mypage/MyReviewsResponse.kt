package com.jparkbro.model.mypage

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.ReviewItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyReviewsResponse(
    @SerialName("count")
    val count: Int,
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("reviews")
    val reviews: List<MyReviewItem>
)

@Serializable
data class MyReviewItem(
    @SerialName("reviewId")
    override val reviewId: Int,
    @SerialName("animeId")
    val animeId: Int,
    @SerialName("title")
    val title: String?,
    @SerialName("coverImageUrl")
    val coverImageUrl: String,
    @SerialName("rating")
    override val rating: Float = 0f,
    @SerialName("reviewContent")
    val reviewContent: String?,
    @SerialName("createdAt")
    override val createdAt: String,
    @SerialName("likeCount")
    override val likeCount: Int = 0,
    @SerialName("isLiked")
    override val isLiked: Boolean = false,
): ReviewItem {
    override val content: String? get() = reviewContent
    override val nickname: String get() = ""
    override val isMine: Boolean get() = true
    override val profileImageUrl: String? get() = ""
}
