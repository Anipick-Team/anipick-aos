package com.jparkbro.home.main

interface HomeAction {
    data object OnRetryClicked : HomeAction
    data object NavigateToSearch : HomeAction
    data object NavigateToTrending : HomeAction
    data object NavigateToRecommend : HomeAction
    data object NavigateToReview : HomeAction
    data class NavigateToNextQuarter(val year: String, val quarter: String) : HomeAction
    data object NavigateToSimilar : HomeAction
    data object NavigateToUpcoming : HomeAction
    data class NavigateToAnimeDetail(val animeId: Int) : HomeAction
}