package com.jparkbro.info.series

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.anime.Anime

data class InfoSeriesState(
    val uiState: UiState = UiState.Loading,
    val title: String = "",

    /* API 통신 로딩 */
    val isLoading: Boolean = false,
    val hasMoreData: Boolean = true,

    /* API 통신 데이터 */
    val totalCount: Int = 0,
    val cursor: Cursor? = null,
    val animes: List<Anime> = emptyList()
)