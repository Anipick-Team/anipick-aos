package com.jparkbro.ranking

import com.jparkbro.ui.model.BottomSheetParams

interface RankingAction {
    data object OnRetryClicked : RankingAction
    data object NavigateToSearch : RankingAction
    data class NavigateToInfoAnime(val animeId: Long) : RankingAction
    data object OnLoadMore : RankingAction
    data object OnFilterRealTimeClicked : RankingAction
    data object OnFilterYearQuarterClicked : RankingAction
    data object OnFilterAllTimeClicked : RankingAction
    data object OnFilterGenreClicked : RankingAction
    data class OnYearQuarterCompleteClicked(val params: BottomSheetParams) : RankingAction
    data class OnGenreCompleteClicked(val params: BottomSheetParams) : RankingAction
}