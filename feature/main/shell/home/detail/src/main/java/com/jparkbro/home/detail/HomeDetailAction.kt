package com.jparkbro.home.detail

import com.jparkbro.model.common.FormType
import com.jparkbro.model.enum.HomeDetailSortType

interface HomeDetailAction {
    data object OnRetryClicked : HomeDetailAction
    data class OnChangeSortType(val type: HomeDetailSortType) : HomeDetailAction
    data class OnReviewLikeClicked(val reviewId: Long, val animeId: Long, val isLiked: Boolean, val callback: (Boolean) -> Unit) : HomeDetailAction
    data class OnReviewDeleteClicked(val reviewId: Long) : HomeDetailAction
    data class OnReviewReportClicked(val reviewId: Long) : HomeDetailAction
    data class OnUserBlockClicked(val userId: Long) : HomeDetailAction
    data object OnLoadMore : HomeDetailAction
    data object NavigateBack : HomeDetailAction
    data class NavigateToEditReview(val animeId: Long, val type: FormType) : HomeDetailAction
    data class NavigateToAnimeDetail(val animeId: Long) : HomeDetailAction
}