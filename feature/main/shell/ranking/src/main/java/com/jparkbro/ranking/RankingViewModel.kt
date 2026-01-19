package com.jparkbro.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.model.common.UiState
import com.jparkbro.model.dto.ranking.GetAnimeRankingRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RankingState())
    val state = _state.asStateFlow()

    init {
        initDataLoad()
    }

    fun onAction(action: RankingAction) {
        when(action) {
            RankingAction.OnRetryClicked -> retry()
        }
    }

    private fun initDataLoad(isMoreData: Boolean = false) {
        if (_state.value.isMoreDataLoading || !_state.value.hasMoreData) return

        if (isMoreData) _state.update { it.copy(isMoreDataLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            animeRepository.getAnimeRanking(
                request = GetAnimeRankingRequest(
                    type = _state.value.type,
                    year = _state.value.year,
                    season = _state.value.quarter,
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
                                hasMoreData = result.animes.size >= 20
                            )
                        }
                    }
                },
                onFailure = {
                    if (isMoreData) {
                        // TODO
                        _state.update { it.copy(isMoreDataLoading = false) }
                    } else {
                        _state.update { it.copy(uiState = UiState.Error) }
                    }
                }
            )
        }
    }

    private fun retry() {
        _state.update { RankingState() }

        initDataLoad()
    }
}
