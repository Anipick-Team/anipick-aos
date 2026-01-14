package com.jparkbro.studio

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.anime.Anime

data class StudioState(
    val uiState: UiState = UiState.Loading,

    /* API 통신 로딩 */
    val isLoading: Boolean = false,
    val hasMoreData: Boolean = true,

    /* API 통신 데이터 */
    val studioName: String? = null,
    val cursor: Cursor? = null,
    val animes: List<Anime> = emptyList(),
)
