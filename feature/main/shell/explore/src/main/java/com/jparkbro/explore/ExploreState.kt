package com.jparkbro.explore

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.enum.ExploreSortType
import com.jparkbro.model.enum.GenreOp

data class ExploreState(
    val uiState: UiState = UiState.Loading,
    val year: String? = null,
    val quarter: ResponseMap = ResponseMap(),
    val genres: List<ResponseMap> = emptyList(),
    val genreOp: GenreOp = GenreOp.OR,
    val type: String? = null,
    val sort: ExploreSortType = ExploreSortType.POPULARITY,

    /* API 통신 로딩 */
    val isLoading: Boolean = false,
    val isMoreDataLoading: Boolean = false,
    val hasMoreData: Boolean = true,

    /* API 통신 데이터 */
    val animes: List<Anime> = emptyList(),
    val cursor: Cursor? = null
)
