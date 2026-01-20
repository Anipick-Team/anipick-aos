package com.jparkbro.ranking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.common.UiState
import com.jparkbro.model.dto.ranking.GetAnimeRankingRequest
import com.jparkbro.model.enum.BottomSheetType
import com.jparkbro.model.enum.RankingType
import com.jparkbro.ui.model.BottomSheetData
import com.jparkbro.ui.model.BottomSheetParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RankingState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<RankingEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        dataLoad()
    }

    fun onAction(action: RankingAction) {
        when(action) {
            RankingAction.OnRetryClicked -> retry()
            RankingAction.OnLoadMore -> dataLoad(true)
            RankingAction.OnFilterRealTimeClicked -> onFilterRealTimeClicked()
            RankingAction.OnFilterYearQuarterClicked -> onFilterYearQuarterClicked()
            RankingAction.OnFilterAllTimeClicked -> onFilterAllTimeClicked()
            RankingAction.OnFilterGenreClicked -> onFilterGenreClicked()
            is RankingAction.OnYearQuarterCompleteClicked -> onYearQuarterCompleteClicked(action.params)
            is RankingAction.OnGenreCompleteClicked -> onGenreCompleteClicked(action.params)
        }
    }

    private fun dataLoad(isMoreData: Boolean = false) {
        if (_state.value.isMoreDataLoading || !_state.value.hasMoreData) return

        if (isMoreData) _state.update { it.copy(isMoreDataLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            animeRepository.getAnimeRanking(
                request = GetAnimeRankingRequest(
                    type = _state.value.type,
                    year = _state.value.year,
                    season = _state.value.quarter.name,
                    genre = _state.value.genre,
                    lastId = _state.value.lastId,
                    lastValue = _state.value.lastValue,
                    lastRank = _state.value.lastRank,
                )
            ).fold(
                onSuccess = { result ->
                    if (isMoreData) {
                        _state.update {
                            it.copy(
                                isMoreDataLoading = false,
                                animes = it.animes + result.animes,
                                lastId = result.cursor.lastId,
                                lastValue = result.cursor.lastValue?.toLong(),
                                lastRank = result.animes.lastOrNull()?.rank,
                                hasMoreData = result.animes.size >= 20,
                                isLoading = false
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                uiState = UiState.Success,
                                animes = result.animes,
                                lastId = result.cursor.lastId,
                                lastValue = result.cursor.lastValue?.toLong(),
                                lastRank = result.animes.lastOrNull()?.rank,
                                hasMoreData = result.animes.size >= 20,
                                isLoading = false
                            )
                        }
                    }
                },
                onFailure = {
                    if (isMoreData) {
                        // TODO
                        _state.update { it.copy(
                            isMoreDataLoading = false,
                            isLoading = false
                        ) }
                    } else {
                        _state.update { it.copy(
                            uiState = UiState.Error,
                            isLoading = false
                        ) }
                    }
                }
            )
        }
    }

    private fun onFilterRealTimeClicked() {
        viewModelScope.launch(Dispatchers.Main) {
            _state.update {
                RankingState(
                    uiState = UiState.Success,
                    type = RankingType.REAL_TIME,
                    genre = it.genre,
                    isLoading = true
                )
            }

            dataLoad()
        }
    }

    private fun onFilterYearQuarterClicked() {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                RankingEvent.ShowBottomSheet(
                    BottomSheetData(
                        sheetType = BottomSheetType.YEAR_QUARTER,
                        params = BottomSheetParams(
                            year = _state.value.year ?: "전체년도",
                            quarter = _state.value.quarter,
                            genres = listOf(_state.value.genre),
                        ),
                        includeYearQuarter = true
                    )
                )
            )
        }
    }

    private fun onFilterAllTimeClicked() {
        viewModelScope.launch(Dispatchers.Main) {
            _state.update {
                RankingState(
                    uiState = UiState.Success,
                    type = RankingType.ALL_TIME,
                    genre = it.genre,
                    isLoading = true
                )
            }

            dataLoad()
        }
    }

    private fun onFilterGenreClicked() {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                RankingEvent.ShowBottomSheet(
                    BottomSheetData(
                        sheetType = BottomSheetType.GENRE,
                        params = BottomSheetParams(
                            year = _state.value.year ?: "전체년도",
                            quarter = _state.value.quarter,
                            genres = listOf(_state.value.genre),
                        ),
                        includeGenres = true
                    )
                )
            )
        }
    }

    private fun onYearQuarterCompleteClicked(params: BottomSheetParams) {
        viewModelScope.launch(Dispatchers.Main) {
            _state.update {
                it.copy(
                    type = RankingType.YEAR_SEASON,
                    year = params.year,
                    quarter = params.quarter,
                    isLoading = true,
                    hasMoreData = true,
                    animes = emptyList(),
                    lastId = null,
                    lastValue = null,
                    lastRank = null
                )
            }
            dataLoad()
        }
    }

    private fun onGenreCompleteClicked(params: BottomSheetParams) {
        viewModelScope.launch(Dispatchers.Main) {
            _state.update {
                it.copy(
                    genre = params.genres.firstOrNull() ?: ResponseMap(),
                    isLoading = true,
                    hasMoreData = true,
                    animes = emptyList(),
                    lastId = null,
                    lastValue = null,
                    lastRank = null
                )
            }
            dataLoad()
        }
    }

    private fun retry() {
        _state.update { RankingState() }

        dataLoad()
    }
}
