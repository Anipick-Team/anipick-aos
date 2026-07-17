package com.jparkbro.search.main

interface SearchAction {
    data object OnRetryClicked : SearchAction
    data object NavigateBack : SearchAction
    data class NavigateToInfoAnime(val animeId: Long) : SearchAction
    data class NavigateToSearchResult(val keyword: String) : SearchAction
    data object OnSearchClicked : SearchAction
    data object OnClearSearchKeyword : SearchAction
    data class OnDeleteSearchKeyword(val keyword: String) : SearchAction
    data object OnDeleteAllSearchKeywords : SearchAction
}