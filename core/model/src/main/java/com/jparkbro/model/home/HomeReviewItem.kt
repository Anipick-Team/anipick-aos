package com.jparkbro.model.home

import com.jparkbro.model.common.ReviewItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeReviewItem(
    @SerialName("userId")
    val userId: Long? = null,
    @SerialName("reviewId")
    override val reviewId: Long,
    @SerialName("animeId")
    val animeId: Long,
    @SerialName("animeTitle")
    val animeTitle: String?,
    @SerialName("animeCoverImageUrl")
    val animeCoverImageUrl: String? = null,
    @SerialName("rating")
    override val rating: Float = 0f,
    @SerialName("reviewContent")
    val reviewContent: String,
    @SerialName("nickname")
    override val nickname: String,
    @SerialName("createdAt")
    override val createdAt: String,
    @SerialName("likeCount")
    override val likeCount: Int = 0,
    @SerialName("likedByCurrentUser")
    override val isLiked: Boolean = false,
    @SerialName("isMine")
    override val isMine: Boolean = false,
    @SerialName("profileImageUrl")
    override val profileImageUrl: String? = null,
) : ReviewItem {
    override val content: String? get() = reviewContent
}
