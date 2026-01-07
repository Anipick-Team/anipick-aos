package com.jparkbro.info.recommend

interface InfoRecommendAction {
    data object OnRetryClicked : InfoRecommendAction
    data object NavigateBack : InfoRecommendAction
    data class NavigateToInfoAnime(val animeId: Long) : InfoRecommendAction
    data object LoadMoreAnime : InfoRecommendAction
}