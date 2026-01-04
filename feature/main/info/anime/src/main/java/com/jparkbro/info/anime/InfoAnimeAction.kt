package com.jparkbro.info.anime

import com.jparkbro.model.common.FormType
import com.jparkbro.model.enum.AnimeInfoTab
import com.jparkbro.model.enum.ReviewSortType
import com.jparkbro.model.enum.WatchStatus

interface InfoAnimeAction {
    data object OnRetryClicked : InfoAnimeAction
    data object NavigateBack : InfoAnimeAction
    data class OnTabClicked(val tab: AnimeInfoTab) : InfoAnimeAction
    data object LoadMoreReviews : InfoAnimeAction
    data class OnAnimeLikeClicked(val state: Boolean) : InfoAnimeAction
    data object OnShareClicked : InfoAnimeAction
    data class OnWatchStatusClicked(val status: WatchStatus) : InfoAnimeAction
    data object NavigateToStudio : InfoAnimeAction
    data object NavigateToCasts : InfoAnimeAction
    data object NavigateToSeries : InfoAnimeAction
    data object NavigateToRecommend : InfoAnimeAction
    data class NavigateToActor(val actorId: Int) : InfoAnimeAction
    data class NavigateToAnimeDetail(val animeId: Int) : InfoAnimeAction
    data class OnRatingChanged(val rating: Float) : InfoAnimeAction
    data class OnChangeReviewSortType(val sortType: ReviewSortType) : InfoAnimeAction
    data class OnReviewLikeClicked(val reviewId: Int, val isLiked: Boolean, val callback: (Boolean) -> Unit) : InfoAnimeAction
    data class OnReviewDeleteClicked(val reviewId: Int) : InfoAnimeAction
    data class OnReviewEditClicked(val animeId: Int, val reviewId: Int, val type: FormType) : InfoAnimeAction
    data class OnReviewReportClicked(val reviewId: Int) : InfoAnimeAction
    data class OnUserBlockClicked(val userId: Int) : InfoAnimeAction
}