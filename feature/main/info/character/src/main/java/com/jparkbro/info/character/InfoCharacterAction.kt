package com.jparkbro.info.character

interface InfoCharacterAction {
    data object OnRetryClicked : InfoCharacterAction
    data object NavigateBack : InfoCharacterAction
    data class NavigateToActor(val personId: Int) : InfoCharacterAction
    data object LoadMoreCasts : InfoCharacterAction
}