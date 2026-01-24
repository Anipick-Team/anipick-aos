package com.jparkbro.explore

import com.jparkbro.model.enum.BottomSheetType
import com.jparkbro.model.enum.ExploreSortType
import com.jparkbro.ui.model.BottomSheetParams

interface ExploreAction {
    data object OnRetryClicked : ExploreAction
    data object NavigateToSearch : ExploreAction
    data class NavigateToInfoAnime(val animeId: Long) : ExploreAction
    data object OnLoadMore : ExploreAction
    data class OnFilterChipClicked(val type: BottomSheetType) : ExploreAction
    data class OnSortChanged(val sortType: ExploreSortType) : ExploreAction
    data class OnBottomSheetCompleteClicked(val params: BottomSheetParams) : ExploreAction
    data object OnYearFilterCancelClicked : ExploreAction
    data object OnQuarterFilterCancelClicked : ExploreAction
    data class OnGenreFilterCancelClicked(val genreId: Int) : ExploreAction
    data object OnTypeFilterCancelClicked : ExploreAction
}