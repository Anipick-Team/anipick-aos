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
    companion object {
        private const val TAG = "HomeViewModel"
    }

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
        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.recentReviews.collect { reviews ->
                _state.update { it.copy(recentReviews = reviews) }
            }
        }
    }

    private fun collectRecentRecommendAnimes() {
        viewModelScope.launch(Dispatchers.IO) {
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
        Log.d(TAG, "initDataLoad: 시작")
        _state.update {
            it.copy(
                uiState = UiState.Loading
            )
        }

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

                Log.d(TAG, "initDataLoad: 모든 API 호출 성공")
                _state.update {
                    it.copy(
                        uiState = UiState.Success
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "initDataLoad: 실패", e)
                _state.update {
                    it.copy(
                        uiState = UiState.Error // TODO 에러 종류 분기 처리
                    )
                }
            }
        }
    }

    private suspend fun getNickname() {
        Log.d(TAG, "getNickname: 시작")
        userPreferenceRepository.getUserNickName()
            .fold(
                onSuccess = { nickname ->
                    Log.d(TAG, "getNickname: 성공 - nickname=$nickname")
                    _state.update {
                        it.copy(
                            nickname = nickname
                        )
                    }
                },
                onFailure = {
                    Log.e(TAG, "getNickname: 실패", it)
                    throw it
                }
            )
    }

    private suspend fun getRecentAnime() {
        animeRepository.loadRecentAnime()
            .onSuccess { animeId ->
                Log.d(TAG, "getRecentAnime: 성공")
                _state.update {
                    it.copy(
                        recentAnime = animeId
                    )
                }
                getSimilarAnimes()
            }
            .onFailure {
                Log.e(TAG, "getRecentAnime: 실패", it)
                throw it
            }
    }

    private suspend fun getTrendingAnimes() {
        Log.d(TAG, "getTrendingAnimes: 시작")
        homeRepository.getTrendItems()
            .fold(
                onSuccess = { animes ->
                    Log.d(TAG, "getTrendingAnimes: 성공 - size=${animes.size}")
                    _state.update {
                        it.copy(
                            trendingAnimeDtos = animes
                        )
                    }
                },
                onFailure = {
                    Log.e(TAG, "getTrendingAnimes: 실패", it)
                    throw it
                }
            )
    }

    private suspend fun getRecommendAnimes() {
        Log.d(TAG, "getRecommendAnimes: 시작")
        homeRepository.getRecommendItems()
            .fold(
                onSuccess = { response ->
                    Log.d(TAG, "getRecommendAnimes: 성공 - size=${response.animes.size}, title=${response.referenceAnimeTitle}")
                    _state.update {
                        it.copy(
                            recommendedAnimes = response.animes,
                            referenceAnimeTitle = response.referenceAnimeTitle
                        )
                    }
                },
                onFailure = {
                    Log.e(TAG, "getRecommendAnimes: 실패", it)
                    throw it
                }
            )
    }

    private suspend fun getNextQuarterAnimes() {
        Log.d(TAG, "getNextQuarterAnimes: 시작")
        homeRepository.getNextQuarterAnimes()
            .fold(
                onSuccess = { response ->
                    Log.d(TAG, "getNextQuarterAnimes: 성공 - size=${response.animes.size}, year=${response.seasonYear}, season=${response.season}")
                    _state.update {
                        it.copy(
                            nextQuarterAnimes = response.animes,
                            year = response.seasonYear ?: 0,
                            season = response.season ?: 0
                        )
                    }
                },
                onFailure = {
                    Log.e(TAG, "getNextQuarterAnimes: 실패", it)
                    throw it
                }
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
        Log.d(TAG, "getUpcomingAnimes: 시작")
        homeRepository.getComingSoonItems()
            .fold(
                onSuccess = { animes ->
                    Log.d(TAG, "getUpcomingAnimes: 성공 - size=${animes.size}")
                    _state.update {
                        it.copy(
                            upcomingAnimes = animes
                        )
                    }
                },
                onFailure = {
                    Log.e(TAG, "getUpcomingAnimes: 실패", it)
                    throw it
                }
            )
    }
}