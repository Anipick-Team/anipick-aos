package com.jparkbro.ranking

interface RankingAction {
    data object OnRetryClicked : RankingAction
    data object NavigateToSearch : RankingAction
    data class NavigateToInfoAnime(val animeId: Long) : RankingAction
}