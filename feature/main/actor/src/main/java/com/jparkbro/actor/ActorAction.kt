package com.jparkbro.actor

interface ActorAction {
    data object OnRetryClicked : ActorAction
    data object NavigateBack : ActorAction
    data class NavigateToInfoAnime(val animeId: Long) : ActorAction
    data object LoadMoreAnime : ActorAction
    data class OnActorLikeClicked(val isLiked: Boolean) : ActorAction
}