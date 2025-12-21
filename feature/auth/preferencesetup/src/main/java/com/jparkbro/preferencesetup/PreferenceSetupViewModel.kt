package com.jparkbro.preferencesetup

import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.AuthRepository
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.common.UiState
import com.jparkbro.model.dto.preference.RatedAnime
import com.jparkbro.model.dto.preference.SearchRequest
import com.jparkbro.model.enum.BottomSheetType
import com.jparkbro.model.exception.ApiException
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
class PreferenceSetupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PreferenceSetupState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<PreferenceSetupEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        loadInitialAnimes()
    }

    fun onAction(action: PreferenceSetupAction) {
        when (action) {
            PreferenceSetupAction.OnRetryClicked -> retry()
            PreferenceSetupAction.OnClearTextClicked -> {
                _state.update {
                    it.copy(
                        searchText = it.searchText.apply { clearText() }
                    )
                }
            }
            PreferenceSetupAction.OnClearFilterClicked -> {
                _state.update {
                    it.copy(
                        yearFilter = "",
                        quarterFilter = ResponseMap(),
                        genreFilter = ResponseMap()
                    )
                }
            }
            PreferenceSetupAction.OnSearchClicked -> searchAnimes()
            PreferenceSetupAction.OnLoadMore -> loadMoreAnimes()
            PreferenceSetupAction.OnSkipClicked -> submitReviews(true)
            PreferenceSetupAction.OnCompleteClicked -> submitReviews(false)
            is PreferenceSetupAction.OnFilterChipClicked -> { showBottomSheet(action.type) }
            is PreferenceSetupAction.OnFilterCompleteClicked -> {
                _state.update {
                    it.copy(
                        yearFilter = action.params.year,
                        quarterFilter = action.params.quarter,
                        genreFilter = action.params.genres.firstOrNull() ?: ResponseMap(),
                    )
                }
                searchAnimes()
            }
            is PreferenceSetupAction.OnRatingAddClicked -> { addRateAnime(action.rate) }
            is PreferenceSetupAction.OnRatingRemoveClicked -> { removeRateAnime(action.animeId) }
            else -> Unit
        }
    }

    private fun retry() {
        _state.update {
            it.copy(
                uiState = UiState.Loading
            )
        }
        loadInitialAnimes()
    }

    private fun loadInitialAnimes() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.exploreOrSearch(
                request = SearchRequest()
            ).fold(
                onSuccess = { response ->
                    _state.update {
                        it.copy(
                            animes = response.animes,
                            totalCount = response.count ?: 0,
                            cursor = response.cursor,
                            uiState = UiState.Success
                        )
                    }
                },
                onFailure = { exception ->
                    _state.update {
                        it.copy(uiState = UiState.Error)
                    }
                    when (exception) {
                        is ApiException -> {
                            _eventChannel.send(
                                PreferenceSetupEvent.InitLoadFailed(exception.errorValue)
                            )
                        }
                        else -> Unit
                    }
                }
            )
        }
    }

    private fun searchAnimes() {
        _state.update {
            it.copy(
                animes = emptyList(),
                totalCount = 0,
                cursor = null,
                isMoreAnimeLoading = true,
                hasMoreAnime = true,
                searchQuery = null
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            authRepository.exploreOrSearch(
                request = SearchRequest(
                    query = _state.value.searchText.text.toString(),
                    year = if (_state.value.yearFilter == "전체년도") null else _state.value.yearFilter,
                    season = if (_state.value.quarterFilter.id == -1) null else _state.value.quarterFilter.id,
                    genres = if (_state.value.genreFilter.id == -1) null else _state.value.genreFilter.id,
                )
            ).fold(
                onSuccess = { response ->
                    _state.update {
                        it.copy(
                            animes = response.animes,
                            totalCount = response.count ?: 0,
                            cursor = response.cursor,
                            isMoreAnimeLoading = false,
                            hasMoreAnime = response.animes.size == 10,
                            searchQuery = _state.value.searchText.text.toString()
                        )
                    }
                },
                onFailure = { exception ->
                    when (exception) {
                        is ApiException -> {
                            _eventChannel.send(
                                PreferenceSetupEvent.AnimeLoadFailed(exception.errorValue)
                            )
                        }
                        else -> {
                            _eventChannel.send(
                                PreferenceSetupEvent.AnimeLoadFailed("Unknown error") // TODO
                            )
                        }
                    }
                    _state.update {
                        it.copy(
                            isMoreAnimeLoading = false,
                        )
                    }
                }
            )
        }
    }

    private fun loadMoreAnimes() {
        _state.update {
            it.copy(
                isMoreAnimeLoading = true,
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            authRepository.exploreOrSearch(
                request = SearchRequest(
                    query = _state.value.searchQuery,
                    year = if (_state.value.yearFilter == "전체년도") null else _state.value.yearFilter,
                    season = if (_state.value.quarterFilter.id == -1) null else _state.value.quarterFilter.id,
                    genres = if (_state.value.genreFilter.id == -1) null else _state.value.genreFilter.id,
                    lastId = _state.value.cursor?.lastId,
                )
            ).fold(
                onSuccess = { response ->
                    _state.update {
                        it.copy(
                            animes = it.animes + response.animes,
                            totalCount = response.count ?: 0,
                            cursor = response.cursor,
                            hasMoreAnime = response.animes.size == 10,
                            isMoreAnimeLoading = false
                        )
                    }
                },
                onFailure = { exception ->
                    when (exception) {
                        is ApiException -> {
                            _eventChannel.send(
                                PreferenceSetupEvent.AnimeLoadFailed(exception.errorValue)
                            )
                        }
                        else -> {
                            _eventChannel.send(
                                PreferenceSetupEvent.AnimeLoadFailed("Unknown error") // TODO
                            )
                        }
                    }
                    _state.update {
                        it.copy(
                            isMoreAnimeLoading = false
                        )
                    }
                }
            )
        }
    }

    private fun submitReviews(skip: Boolean) {
        _state.update {
            it.copy(
                isRating = true
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            authRepository.submitReviews(
                request = if (skip) emptyList() else _state.value.ratedAnimes
            ).fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isRating = false
                        )
                    }
                    _eventChannel.send(
                        PreferenceSetupEvent.SubmitSuccess
                    )
                },
                onFailure = { exception ->
                    when (exception) {
                        is ApiException -> {
                            _eventChannel.send(
                                PreferenceSetupEvent.SubmitFailed(exception.errorValue)
                            )
                        }
                        else -> {
                            _eventChannel.send(
                                PreferenceSetupEvent.SubmitFailed("Unknown error") // TODO
                            )
                        }
                    }
                    _state.update {
                        it.copy(
                            isRating = false
                        )
                    }
                }
            )
        }
    }

    private fun showBottomSheet(sheetType: BottomSheetType) {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                PreferenceSetupEvent.ShowBottomSheet(
                    BottomSheetData(
                        sheetType = sheetType,
                        params = BottomSheetParams(
                            year = _state.value.yearFilter,
                            quarter = _state.value.quarterFilter,
                            genres = listOf(_state.value.genreFilter),
                        )
                    )
                )
            )
        }
    }

    private fun addRateAnime(rate: RatedAnime) {
        _state.update { currentState ->
            val currentList = currentState.ratedAnimes.toMutableList()
            val existingIndex = currentList.indexOfFirst { it.animeId == rate.animeId }

            if (existingIndex != -1) {
                currentList[existingIndex] = rate
            } else {
                currentList.add(rate)
            }

            currentState.copy(ratedAnimes = currentList)
        }
    }

    private fun removeRateAnime(animeId: Int) {
        _state.update {
            it.copy(
                ratedAnimes = it.ratedAnimes.filter { it.animeId != animeId }
            )
        }
    }
}