package com.jparkbro.model.common.review

data class Review(
    val reviewId: Int? = null,
    val userId: Int? = null,
    val animeId: Int? = null,
    val animeTitle: String? = null,
    val animeCoverImageUrl: String? = null,
    val content: String? = null,
    val nickname: String? = null,
    val profileImageUrl: String? = null,
    val createdAt: String? = null,
    val rating: Float? = null,
    val likeCount: Int? = null,
    val likedByCurrentUser: Boolean = false,
    val isMine: Boolean = false,
    val isSpoiler: Boolean = false,
    val isLiked: Boolean = false,
)
