package com.jparkbro.model.common

interface ReviewItem {
    val reviewId: Int
    val rating: Float
    val content: String?
    val nickname: String
    val createdAt: String
    val isMine: Boolean
    val likeCount: Int
    val profileImageUrl: String?
    val isLiked: Boolean
}