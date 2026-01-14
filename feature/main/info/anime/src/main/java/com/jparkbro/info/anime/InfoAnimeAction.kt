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
    data class OnAnimeLikeClicked(val isLiked: Boolean) : InfoAnimeAction
    data class OnShareClicked(val shareLink: String) : InfoAnimeAction
    data class OnWatchStatusClicked(val watchStatus: WatchStatus) : InfoAnimeAction
    data class NavigateToStudio(val studioId: Long) : InfoAnimeAction
    data class NavigateToCasts(val animeId: Long) : InfoAnimeAction
    data class NavigateToSeries(val animeId: Long, val title: String) : InfoAnimeAction
    data class NavigateToRecommend(val animeId: Long) : InfoAnimeAction
    data class NavigateToActor(val actorId: Long) : InfoAnimeAction
    data class NavigateToAnimeDetail(val animeId: Long) : InfoAnimeAction
    data class OnRatingChanged(val rating: Float, val onFailure: () -> Unit) : InfoAnimeAction
    data class OnChangeReviewSortType(val sortType: ReviewSortType) : InfoAnimeAction
    data class OnReviewLikeClicked(val reviewId: Long, val animeId: Long, val isLiked: Boolean, val callback: (Boolean) -> Unit) : InfoAnimeAction
    data class NavigateToEditReview(val animeId: Long, val type: FormType) : InfoAnimeAction
    data class OnReviewDeleteClicked(val reviewId: Long) : InfoAnimeAction
    data class OnReviewReportClicked(val reviewId: Long) : InfoAnimeAction
    data class OnUserBlockClicked(val userId: Long) : InfoAnimeAction
}