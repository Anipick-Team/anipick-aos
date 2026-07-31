package com.jparkbro.home.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.data.home.HomeRepository
import com.jparkbro.data.review.ReviewRepository
import com.jparkbro.model.common.UiState
import com.jparkbro.model.exception.NetworkException
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
            HomeAction.ShowNotice -> _state.update { it.copy(showNoticeDialog = true) }
            HomeAction.DismissNotice -> _state.update { it.copy(showNoticeDialog = false) }
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
                Log.d(TAG, "collectRecentRecommendAnimes: collected result=$result, animes=${result?.animes?.size ?: 0}")
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
            } catch (e: NetworkException) {
                _state.update { it.copy(uiState = UiState.Error,) }
            } catch (e: Exception) {
                _state.update { it.copy(uiState = UiState.Success,) }
            }
        }
    }

    private suspend fun getNickname() {
        Log.d(TAG, "getNickname: start")
        userPreferenceRepository.getUserNickName()
            .fold(
                onSuccess = { nickname ->
                    Log.d(TAG, "getNickname: success, nickname=$nickname")
                    _state.update {
                        it.copy(
                            nickname = nickname
                        )
                    }
                },
                onFailure = {
                    Log.e(TAG, "getNickname: failure", it)
                    throw it
                }
            )
    }

    private suspend fun getRecentAnime() {
        Log.d(TAG, "getRecentAnime: start")
        animeRepository.loadRecentAnime()
            .onSuccess { animeId ->
                Log.d(TAG, "getRecentAnime: success, animeId=$animeId")
                _state.update { it.copy(recentAnime = animeId) }
                getSimilarAnimes()
            }
            .onFailure {
                Log.e(TAG, "getRecentAnime: failure", it)
                throw it
            }
    }

    private suspend fun getTrendingAnimes() {
        Log.d(TAG, "getTrendingAnimes: start")
        homeRepository.getTrendItems()
            .fold(
                onSuccess = { animes ->
                    Log.d(TAG, "getTrendingAnimes: success, count=${animes.size}")
                    _state.update { it.copy(trendingAnimeDtos = animes) }
                },
                onFailure = {
                    Log.e(TAG, "getTrendingAnimes: failure", it)
                    throw it
                }
            )
    }

    private suspend fun getRecommendAnimes() {
        Log.d(TAG, "getRecommendAnimes: start")
        homeRepository.getRecommendItems()
            .fold(
                onSuccess = { response ->
                    Log.d(TAG, "getRecommendAnimes: success, count=${response.animes.size}, referenceTitle=${response.referenceAnimeTitle}")
                    _state.update {
                        it.copy(
                            recommendedAnimes = response.animes,
                            referenceAnimeTitle = response.referenceAnimeTitle
                        )
                    }
                },
                onFailure = {
                    Log.e(TAG, "getRecommendAnimes: failure", it)
                    throw it
                }
            )
    }

    private suspend fun getNextQuarterAnimes() {
        Log.d(TAG, "getNextQuarterAnimes: start")
        homeRepository.getNextQuarterAnimes()
            .fold(
                onSuccess = { response ->
                    Log.d(TAG, "getNextQuarterAnimes: success, count=${response.animes.size}, year=${response.seasonYear}, season=${response.season}")
                    _state.update {
                        it.copy(
                            nextQuarterAnimes = response.animes,
                            year = response.seasonYear ?: 0,
                            season = response.season ?: 0
                        )
                    }
                },
                onFailure = {
                    Log.e(TAG, "getNextQuarterAnimes: failure", it)
                    throw it
                }
            )
    }

    private suspend fun getSimilarAnimes() {
        Log.d(TAG, "getSimilarAnimes: start")
        val recentAnime = _state.value.recentAnime

        if (recentAnime == -1L) {
            Log.d(TAG, "getSimilarAnimes: no recent anime, returning empty list")
            _state.update { it.copy(similarAnimes = emptyList()) }
        } else {
            Log.d(TAG, "getSimilarAnimes: fetching similar animes for recentAnime=$recentAnime")
            animeRepository.getRecentRecommendItems(recentAnime)
                .onSuccess {
                    Log.d(TAG, "getSimilarAnimes: success")
                }
                .onFailure {
                    Log.e(TAG, "getSimilarAnimes: failure", it)
                    throw it
                }
        }
    }

    private suspend fun getUpcomingAnimes() {
        Log.d(TAG, "getUpcomingAnimes: start")
        homeRepository.getComingSoonItems()
            .fold(
                onSuccess = { animes ->
                    Log.d(TAG, "getUpcomingAnimes: success, count=${animes.size}")
                    _state.update {
                        it.copy(
                            upcomingAnimes = animes
                        )
                    }
                },
                onFailure = {
                    Log.e(TAG, "getUpcomingAnimes: failure", it)
                    throw it
                }
            )
    }
}