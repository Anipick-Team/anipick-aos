package com.jparkbro.actor

import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.actor.Filmography
import com.jparkbro.model.dto.actor.GetActorResult

data class ActorState(
    val uiState: UiState = UiState.Loading,

    /* API 통신 로딩 */
    val isLoading: Boolean = false,
    val hasMoreData: Boolean = true,
    val isLikedLoading: Boolean = false,

    /* API 통신 데이터 */
    val result: GetActorResult = GetActorResult(),
    val works: List<Filmography> = emptyList()
)
