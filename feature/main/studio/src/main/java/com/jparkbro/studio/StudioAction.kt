package com.jparkbro.studio

interface StudioAction {
    data object OnRetryClicked : StudioAction
    data object NavigateBack : StudioAction
    data class NavigateToInfoAnime(val animeId: Long) : StudioAction
    data object LoadMoreAnime : StudioAction
}