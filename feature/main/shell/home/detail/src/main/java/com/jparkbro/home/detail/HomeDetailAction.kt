package com.jparkbro.home.detail

import com.jparkbro.model.common.FormType
import com.jparkbro.model.enum.HomeDetailSortType

interface HomeDetailAction {
    data object OnRetryClicked : HomeDetailAction
    data class OnSortClicked(val type: HomeDetailSortType) : HomeDetailAction
    data class OnReviewLikeClicked(val reviewId: Int, val isLiked: Boolean, val callback: (Boolean) -> Unit) : HomeDetailAction
    data class OnReviewDeleteClicked(val reviewId: Int) : HomeDetailAction
    data class OnReviewEditClicked(val animeId: Int, val reviewId: Int, val type: FormType) : HomeDetailAction
    data class OnReviewReportClicked(val reviewId: Int) : HomeDetailAction
    data class OnUserBlockClicked(val userId: Int) : HomeDetailAction
    data object OnLoadMore : HomeDetailAction
    data object NavigateBack : HomeDetailAction
    data class NavigateToAnimeDetail(val animeId: Int) : HomeDetailAction
}