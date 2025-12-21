package com.jparkbro.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.home.HomeRepository
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.data.detail.DetailRepository
import com.jparkbro.model.home.HomeRecommendResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val detailRepository: DetailRepository,
) : ViewModel() {

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname.asStateFlow()

    // 실시간
    private val _trendItems = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val trendItems: StateFlow<HomeUiState> = _trendItems.asStateFlow()

    // 일반추천
    private val _recommend = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val recommend: StateFlow<HomeUiState> = _recommend.asStateFlow()

    // 최근 확인 애니와 유사한 추천
    private val _recentRecommend = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val recentRecommend: StateFlow<HomeUiState> = _recentRecommend.asStateFlow()

    // 최근리뷰
    private val _recentReviews = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val recentReviews: StateFlow<HomeUiState> = _recentReviews.asStateFlow()

    // 다음분기 방영예
    private val _upcomingSeasonItems = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val upcomingSeasonItems: StateFlow<HomeUiState> = _upcomingSeasonItems.asStateFlow()

    // 공개예정
    private val _comingSoonItems = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val comingSoonItems: StateFlow<HomeUiState> = _comingSoonItems.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            userPreferenceRepository.getUserNickName().onSuccess {
                _nickname.value = it
            }

            homeRepository.getTrendItems().fold(
                onSuccess = {
                    _trendItems.value = HomeUiState.Success(it)
                },
                onFailure = {
                    _trendItems.value = HomeUiState.Error("${it.message}")
                }
            )

            homeRepository.getRecommendItems().fold(
                onSuccess = {
                    _recommend.value = HomeUiState.Success(it)
                },
                onFailure = {
                    _recommend.value = HomeUiState.Error("${it.message}")
                }
            )

            detailRepository.loadRecentAnime().fold(
                onSuccess = {
                    if (it == -1) {
                        _recentRecommend.value = HomeUiState.Success(HomeRecommendResponse(animes = emptyList()))
                    } else {
                        homeRepository.getRecentRecommendItems(it).fold(
                            onSuccess = {
                                _recentRecommend.value = HomeUiState.Success(it)
                            },
                            onFailure = {
                                _recentRecommend.value = HomeUiState.Error("${it.message}")
                            }
                        )
                    }
                },
                onFailure = {
                    _recentRecommend.value = HomeUiState.Error("${it.message}")
                }
            )

            homeRepository.getRecentReviews().fold(
                onSuccess = {
                    _recentReviews.value = HomeUiState.Success(it)
                },
                onFailure = {
                    _recentReviews.value = HomeUiState.Error("${it.message}")
                }
            )

            homeRepository.getComingSoonItems().fold(
                onSuccess = {
                    _comingSoonItems.value = HomeUiState.Success(it)
                },
                onFailure = {
                    Log.d("home viewmodel e", "$it")
                }
            )
        }
    }
}

sealed interface HomeUiState {
    data object Loading: HomeUiState
    data class Success<T>(val data: T): HomeUiState
    data class Error(val msg: String): HomeUiState
}