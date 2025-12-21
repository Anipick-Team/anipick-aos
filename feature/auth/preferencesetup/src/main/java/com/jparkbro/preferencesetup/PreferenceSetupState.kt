package com.jparkbro.preferencesetup

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.common.UiState
import com.jparkbro.model.dto.preference.RatedAnime

data class PreferenceSetupState(
    val uiState: UiState = UiState.Loading,
    val searchText: TextFieldState = TextFieldState(),
    // LoadMore 검색어 저장용
    val searchQuery: String? = null,
    val yearFilter: String = "전체년도",
    val quarterFilter: ResponseMap = ResponseMap(name = "전체분기"),
    val genreFilter: ResponseMap = ResponseMap(),
    val ratedAnimes: List<RatedAnime> = emptyList(),
    val isRating: Boolean = false,
    // 애니 조회 (무한스크롤)
    val isMoreAnimeLoading: Boolean = false,
    // 애니 더 조회 가능여부
    val hasMoreAnime: Boolean = true,
    // API 통신
    val animes: List<Anime> = emptyList(),
    val totalCount: Int = 0, // TODO 사용 안함, 삭제해도 괜찮을듯?
    val cursor: Cursor? = null
)

