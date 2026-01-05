package com.jparkbro.info.series

interface InfoSeriesAction {
    data object OnRetryClicked : InfoSeriesAction
    data object NavigateBack : InfoSeriesAction
    data class NavigateToInfoAnime(val animeId: Int) : InfoSeriesAction
    data object LoadMoreAnime : InfoSeriesAction
}