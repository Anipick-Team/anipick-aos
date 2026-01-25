package com.jparkbro.search.detail

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.Studio
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.actor.Person
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.enum.SearchType

data class SearchResultState(
    val uiState: UiState = UiState.Loading,
    val keyword: String? = null,
    val searchKeyword: TextFieldState = TextFieldState(),
    val searchType: SearchType = SearchType.ANIME,

    /* API 통신 로딩 */
    val isLoading: Boolean = false,
    val isMoreDataLoading: Boolean = false,

    val hasAnimeMoreData: Boolean = true,
    val hasActorMoreData: Boolean = true,
    val hasStudioMoreData: Boolean = true,

    /* API 통신 데이터 */
    val animeCursor: Cursor? = null,
    val animeCount: Int = 0,
    val animes: List<Anime> = emptyList(),
    val nextPage: Int? = null,

    val actorCursor: Cursor? = null,
    val actorCount: Int = 0,
    val actors: List<Person> = emptyList(),

    val studioCursor: Cursor? = null,
    val studioCount: Int = 0,
    val studios: List<Studio> = emptyList(),
)
