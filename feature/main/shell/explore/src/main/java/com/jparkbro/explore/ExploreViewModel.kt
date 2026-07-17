package com.jparkbro.explore

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.request.Disposable
import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.common.UiState
import com.jparkbro.model.dto.explore.GetAnimeExploreRequest
import com.jparkbro.model.enum.BottomSheetType
import com.jparkbro.model.enum.ExploreSortType
import com.jparkbro.model.enum.GenreOp
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
class ExploreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val year = savedStateHandle.get<String>("year")
    private val quarter = savedStateHandle.get<String>("quarter")

    private val _state = MutableStateFlow(ExploreState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<ExploreEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        initState()
    }

    fun onAction(action: ExploreAction) {
        when (action) {
            ExploreAction.OnRetryClicked -> retry()
            ExploreAction.OnLoadMore -> loadAnime(isMoreData = true)
            is ExploreAction.OnFilterChipClicked -> onShowBottomSheet(action.type)
            is ExploreAction.OnBottomSheetCompleteClicked -> onBottomSheetComplete(action.params)
            ExploreAction.OnYearFilterCancelClicked -> onYearFilterCancel()
            ExploreAction.OnQuarterFilterCancelClicked -> onQuarterFilterCancel()
            ExploreAction.OnTypeFilterCancelClicked -> onTypeFilterCancel()
            is ExploreAction.OnGenreFilterCancelClicked -> onGenreFilterCancel(action.genreId)
            is ExploreAction.OnSortChanged -> onSortChanged(action.sortType)
        }
    }

    private fun initState() {
        viewModelScope.launch(Dispatchers.Main) {
            if (year != null && quarter != null) {
                _state.update {
                    it.copy(
                        year = year,
                        quarter = ResponseMap(name = quarter)
                    )
                }
            }
            loadAnime()
        }
    }

    private fun loadAnime(isMoreData: Boolean = false) {
        if (_state.value.isMoreDataLoading || !_state.value.hasMoreData) return

        if (isMoreData) { _state.value = _state.value.copy(isMoreDataLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            animeRepository.getAnimeExplore(
                request = GetAnimeExploreRequest(
                    year = _state.value.year,
                    season = _state.value.quarter.name,
                    genres = _state.value.genres,
                    type = _state.value.type,
                    sort = _state.value.sort,
                    lastId = _state.value.cursor?.lastId,
                    genreOp = _state.value.genreOp,
                    lastValue = _state.value.cursor?.lastValue
                )
            ).fold(
                onSuccess = { result ->
                    if (isMoreData) {
                        _state.update {
                            it.copy(
                                isMoreDataLoading = false,
                                animes = it.animes + result.animes,
                                cursor = result.cursor,
                                hasMoreData = result.animes.size >= 18,
                                isLoading = false
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                uiState = UiState.Success,
                                animes = result.animes,
                                cursor = result.cursor,
                                hasMoreData = result.animes.size >= 18,
                                isLoading = false
                            )
                        }
                    }
                },
                onFailure = {
                    if (isMoreData) {
                        // TODO Toast
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

    private fun onShowBottomSheet(sheetType: BottomSheetType) {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                ExploreEvent.ShowBottomSheet(
                    BottomSheetData(
                        sheetType = sheetType,
                        params = BottomSheetParams(
                            year = _state.value.year,
                            quarter = _state.value.quarter,
                            genres = _state.value.genres,
                            type = _state.value.type,
                            isMatchAllConditions = _state.value.genreOp == GenreOp.AND
                        ),
                        includeYearQuarter = true,
                        includeGenres = true,
                        includeTypeFilter = true,
                        allowMultipleSelection = true,
                    )
                )
            )
        }
    }

    private fun onBottomSheetComplete(params: BottomSheetParams) {
        _state.update {
            it.copy(
                year = params.year,
                quarter = params.quarter,
                genres = params.genres,
                genreOp = if (params.isMatchAllConditions) GenreOp.AND else GenreOp.OR,
                type = params.type,
                cursor = null,
                hasMoreData = true,
                isLoading = true,
                animes = emptyList()
            )
        }
        loadAnime()
    }

    private fun onYearFilterCancel() {
        _state.update {
            it.copy(
                year = null,
                quarter = ResponseMap(),
                cursor = null,
                hasMoreData = true,
                isLoading = true,
                animes = emptyList()
            )
        }
        loadAnime()
    }

    private fun onQuarterFilterCancel() {
        _state.update {
            it.copy(
                quarter = ResponseMap(),
                cursor = null,
                hasMoreData = true,
                isLoading = true,
                animes = emptyList()
            )
        }
        loadAnime()
    }

    private fun onTypeFilterCancel() {
        _state.update {
            it.copy(
                type = null,
                cursor = null,
                hasMoreData = true,
                isLoading = true,
                animes = emptyList()
            )
        }
        loadAnime()
    }

    private fun onGenreFilterCancel(genreId: Int) {
        _state.update {
            it.copy(
                genres = it.genres.filter { genre -> genre.id != genreId },
                cursor = null,
                hasMoreData = true,
                isLoading = true,
                animes = emptyList()
            )
        }
        loadAnime()
    }

    private fun onSortChanged(sortType: ExploreSortType) {
        _state.update {
            it.copy(
                sort = sortType,
                cursor = null,
                hasMoreData = true,
                isLoading = true,
                animes = emptyList()
            )
        }
        loadAnime()
    }

    private fun retry() {
        _state.update { it.copy(uiState = UiState.Loading) }

        loadAnime()
    }
}