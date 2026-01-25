package com.jparkbro.search.main

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.anime.Anime

data class SearchState(
    val uiState: UiState = UiState.Loading,
    val searchKeyword: TextFieldState = TextFieldState(),

    /* API 통신 데이터 */
    val recentKeywords: List<String> = emptyList(),
    val animes: List<Anime> = emptyList(),
)
