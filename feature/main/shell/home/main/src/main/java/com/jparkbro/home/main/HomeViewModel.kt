package com.jparkbro.home.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.data.home.HomeRepository
import com.jparkbro.data.review.ReviewRepository
import com.jparkbro.model.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val reviewRepository: ReviewRepository,
    private val homeRepository: HomeRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        collectReviews()
        collectRecentRecommendAnimes()
        initDataLoad()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnRetryClicked -> initDataLoad()
        }
    }

    private fun collectReviews() {
        viewModelScope.launch(Dispatchers.Main) {
            reviewRepository.recentReviews.collect { reviews ->
                _state.update { it.copy(recentReviews = reviews) }
            }
        }
    }

    private fun collectRecentRecommendAnimes() {
        viewModelScope.launch(Dispatchers.Main) {
            animeRepository.recentRecommendAnime.collect { result ->
                _state.update {
                    it.copy(
                        similarAnimeTitle = result?.referenceAnimeTitle,
                        similarAnimes = result?.animes ?: emptyList()
                    )
                }
            }
        }
    }

    private fun initDataLoad() {
        _state.update { it.copy(uiState = UiState.Loading) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                coroutineScope {
                    launch { getNickname() }
                    launch { getRecentAnime() }
                    launch { getTrendingAnimes() } // 실시간 인기 애니메이션
                    launch { getRecommendAnimes() } // 오늘의 추천작
                    launch { reviewRepository.refreshRecentReviews() } // 최근 리뷰
                    launch { getNextQuarterAnimes() } // 다음 분기 애니메이션
                    launch { getUpcomingAnimes() } // 공개 예정
                }

                _state.update {
                    it.copy(
                        uiState = UiState.Success
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        uiState = UiState.Success
                    )
                }
            }
        }
    }

    private suspend fun getNickname() {
        userPreferenceRepository.getUserNickName()
            .fold(
                onSuccess = { nickname ->
                    _state.update {
                        it.copy(
                            nickname = nickname
                        )
                    }
                },
                onFailure = { throw it }
            )
    }

    private suspend fun getRecentAnime() {
        animeRepository.loadRecentAnime()
            .onSuccess { animeId ->
                _state.update {
                    it.copy(
                        recentAnime = animeId
                    )
                }
                getSimilarAnimes()
            }
            .onFailure { throw it }
    }

    private suspend fun getTrendingAnimes() {
        homeRepository.getTrendItems()
            .fold(
                onSuccess = { animes ->
                    _state.update {
                        it.copy(
                            trendingAnimeDtos = animes
                        )
                    }
                },
                onFailure = { throw it }
            )
    }

    private suspend fun getRecommendAnimes() {
        homeRepository.getRecommendItems()
            .fold(
                onSuccess = { response ->
                    _state.update {
                        it.copy(
                            recommendedAnimes = response.animes,
                            referenceAnimeTitle = response.referenceAnimeTitle
                        )
                    }
                },
                onFailure = { throw it }
            )
    }

    private suspend fun getNextQuarterAnimes() {
        homeRepository.getNextQuarterAnimes()
            .fold(
                onSuccess = { response ->
                    _state.update {
                        it.copy(
                            nextQuarterAnimes = response.animes,
                            year = response.seasonYear ?: 0,
                            season = response.season ?: 0
                        )
                    }
                },
                onFailure = { throw it }
            )
    }

    private suspend fun getSimilarAnimes() {
        val recentAnime = _state.value.recentAnime

        if (recentAnime == -1L) {
            _state.update { it.copy(similarAnimes = emptyList()) }
        } else {
            animeRepository.getRecentRecommendItems(recentAnime)
                .onFailure { throw it }
        }
    }

    private suspend fun getUpcomingAnimes() {
        homeRepository.getComingSoonItems()
            .fold(
                onSuccess = { animes ->
                    _state.update {
                        it.copy(
                            upcomingAnimes = animes
                        )
                    }
                },
                onFailure = { throw it }
            )
    }
}