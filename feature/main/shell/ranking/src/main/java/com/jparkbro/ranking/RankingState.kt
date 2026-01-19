package com.jparkbro.ranking

import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.enum.RankingType

data class RankingState(
    val uiState: UiState = UiState.Loading,
    val type: RankingType = RankingType.REAL_TIME,
    val year: String? = null,
    val quarter: String? = null,
    val genre: ResponseMap = ResponseMap(),

    /* API 통신 로딩 */
    val isLoading: Boolean = false,
    val isMoreDataLoading: Boolean = false,
    val hasMoreData: Boolean = true,

    /* API 통신 데이터 */
    val animes: List<Anime> = emptyList(),
    val lastId: Long? = null,
    val lastValue: Long? = null,
    val lastRank: Int? = null,
)
