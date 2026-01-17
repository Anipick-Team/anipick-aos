package com.jparkbro.mypage.detail

import com.jparkbro.model.common.FormType
import com.jparkbro.model.enum.ReviewSortType

interface UserContentAction {
    data object NavigateBack : UserContentAction
    data class NavigateToInfoAnime(val animeId: Long) : UserContentAction
    data class NavigateToActor(val actorId: Long) : UserContentAction
    data class NavigateToEditReview(val animeId: Long, val formType: FormType) : UserContentAction
    data object OnRetryClicked : UserContentAction
    data object OnLoadMore : UserContentAction
    data class OnChangeSortType(val type: ReviewSortType) : UserContentAction
    data class OnReviewLikeClicked(val reviewId: Long, val animeId: Long, val isLiked: Boolean, val callback: (Boolean) -> Unit) : UserContentAction
    data class OnReviewDeleteClicked(val reviewId: Long) : UserContentAction
}