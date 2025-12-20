package com.jparkbro.home.detail

import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.review.Review
import com.jparkbro.model.enum.HomeDetailSortType
import com.jparkbro.model.enum.HomeDetailType

data class HomeDetailState(
    val type: HomeDetailType = HomeDetailType.RECOMMENDS,
    val uiState: UiState = UiState.Loading,
    val animes: List<Anime> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val isMoreDataLoading: Boolean = false,
    val hasMoreData: Boolean = true,
    val recentAnime: Int = -1,
    val referenceAnimeTitle: String? = null,
    val sort: HomeDetailSortType = HomeDetailSortType.LATEST,
    val nickname: String = "방랑자",
    val cursor: Cursor? = null
)
