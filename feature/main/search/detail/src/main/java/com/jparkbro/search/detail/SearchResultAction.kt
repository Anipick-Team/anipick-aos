package com.jparkbro.search.detail

import com.jparkbro.model.enum.SearchType

interface SearchResultAction {
    data object OnRetryClicked : SearchResultAction
    data object NavigateBack : SearchResultAction
    data class OnSubmitAnimeLog(val logUrl: String) : SearchResultAction
    data class NavigateToInfoAnime(val animeId: Long) : SearchResultAction
    data class NavigateToActor(val actorId: Long) : SearchResultAction
    data class NavigateToStudio(val studioId: Long) : SearchResultAction
    data object OnSearchClicked : SearchResultAction
    data object OnClearSearchKeyword : SearchResultAction
    data class OnTabChanged(val type: SearchType) : SearchResultAction
    data object OnLoadMore : SearchResultAction
}