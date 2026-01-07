package com.jparkbro.info.character

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.actor.Person

data class InfoCharacterState(
    val uiState: UiState = UiState.Loading,

    /* API 통신 로딩 */
    val isLoading: Boolean = false,
    val hasMoreData: Boolean = true,

    /* API 통신 데이터 */
    val cursor: Cursor? = null,
    val casts: List<Person> = emptyList()
)
