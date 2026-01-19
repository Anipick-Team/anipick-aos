package com.jparkbro.home.main

import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.review.Review

data class HomeState(
    val uiState: UiState = UiState.Loading,
    val trendingAnimeDtos: List<Anime> = emptyList(),
    val recommendedAnimes: List<Anime> = emptyList(),
    val recentReviews: List<Review> = emptyList(),
    val nextQuarterAnimes: List<Anime> = emptyList(),
    val similarAnimes: List<Anime> = emptyList(),
    val upcomingAnimes: List<Anime> = emptyList(),
    // 최근 확인한 애니메이션
    val recentAnime: Long = -1L,
    val year: Int = 0,
    val season: Int = 0,
    val referenceAnimeTitle: String? = null,
    val similarAnimeTitle: String? = null,
    val nickname: String = "방랑자",
)