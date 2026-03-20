package com.jparkbro.search.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.actor.ActorRepository
import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.data.search.SearchRepository
import com.jparkbro.data.studio.StudioRepository
import com.jparkbro.model.common.UiState
import com.jparkbro.model.dto.search.GetSearchResultRequest
import com.jparkbro.model.enum.SearchType
import com.jparkbro.ui.R
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.snackbar.GlobalSnackbarManager
import com.jparkbro.ui.util.UiText
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
class SearchResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val globalSnackbarManager: GlobalSnackbarManager,
    private val searchRepository: SearchRepository,
    private val animeRepository: AnimeRepository,
    private val actorRepository: ActorRepository,
    private val studioRepository: StudioRepository,
) : ViewModel() {

    private val keyword = savedStateHandle.get<String>("keyword")

    private val _state = MutableStateFlow(
        SearchResultState(
            keyword = keyword,
            searchKeyword = TextFieldState(initialText = keyword ?: "")
        )
    )
    val state = _state.asStateFlow()

    init {
        loadAnime()
        loadActor()
        loadStudio()
    }

    fun onAction(action: SearchResultAction) {
        when (action) {
            SearchResultAction.OnRetryClicked -> retry()
            is SearchResultAction.OnTabChanged -> _state.update { it.copy(searchType = action.type) }
            SearchResultAction.OnSearchClicked -> onSearchClicked()
            SearchResultAction.OnClearSearchKeyword -> _state.update { it.copy(searchKeyword = TextFieldState()) }
            SearchResultAction.OnLoadMore -> {
                when (_state.value.searchType) {
                    SearchType.ANIME -> loadAnime(isLoadMore = true)
                    SearchType.ACTOR -> loadActor(isLoadMore = true)
                    SearchType.STUDIO -> loadStudio(isLoadMore = true)
                }
            }
            is SearchResultAction.OnSubmitAnimeLog -> onSubmitAnimeLog(action.logUrl)
        }
    }

    private fun loadAnime(isLoadMore: Boolean = false) {
        if (_state.value.isMoreDataLoading || !_state.value.hasAnimeMoreData) return

        if (isLoadMore) _state.update { it.copy(isMoreDataLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            val query = _state.value.keyword ?: return@launch
            val request = GetSearchResultRequest(
                query = query,
                lastId = if (isLoadMore) _state.value.animeCursor?.lastId else null,
            )
            animeRepository.getSearchResult(request)
                .fold(
                    onSuccess = { response ->
                        _state.update {
                            it.copy(
                                uiState = UiState.Success,
                                isLoading = false,
                                isMoreDataLoading = false,
                                animeCount = response.count,
                                animeCursor = response.cursor,
                                animes = if (isLoadMore) it.animes + response.animes else response.animes,
                                hasAnimeMoreData = if (isLoadMore) response.count > (it.animes + response.animes).size else response.count > response.animes.size,
                                nextPage = response.nextPage
                            )
                        }
                    },
                    onFailure = {
                        // TODO
                        _state.update {
                            it.copy(
                                uiState = if (isLoadMore) UiState.Success else UiState.Error,
                                isLoading = false,
                                isMoreDataLoading = false
                            )
                        }
                    }
                )
        }
    }

    private fun loadActor(isLoadMore: Boolean = false) {
        if (_state.value.isMoreDataLoading || !_state.value.hasActorMoreData) return

        if (isLoadMore) _state.update { it.copy(isMoreDataLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            val query = _state.value.keyword ?: return@launch
            val request = GetSearchResultRequest(
                query = query,
                lastId = if (isLoadMore) _state.value.actorCursor?.lastId else null,
            )
            actorRepository.getSearchResult(request)
                .fold(
                    onSuccess = { response ->
                        _state.update {
                            it.copy(
                                uiState = UiState.Success,
                                isLoading = false,
                                isMoreDataLoading = false,
                                actorCount = response.count,
                                actorCursor = response.cursor,
                                actors = if (isLoadMore) it.actors + response.persons else response.persons,
                                hasActorMoreData = if (isLoadMore) response.count > (it.actors + response.persons).size else response.count > response.persons.size,
                            )
                        }
                    },
                    onFailure = {
                        // TODO
                        _state.update {
                            it.copy(
                                uiState = if (isLoadMore) UiState.Success else UiState.Error,
                                isLoading = false,
                                isMoreDataLoading = false
                            )
                        }
                    }
                )
        }
    }

    private fun loadStudio(isLoadMore: Boolean = false) {
        if (_state.value.isMoreDataLoading || !_state.value.hasStudioMoreData) return

        if (isLoadMore) _state.update { it.copy(isMoreDataLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            val query = _state.value.keyword ?: return@launch
            val request = GetSearchResultRequest(
                query = query,
                lastId = if (isLoadMore) _state.value.studioCursor?.lastId else null,
            )
            studioRepository.getSearchResult(request)
                .fold(
                    onSuccess = { response ->
                        _state.update {
                            it.copy(
                                uiState = UiState.Success,
                                isLoading = false,
                                isMoreDataLoading = false,
                                studioCount = response.count,
                                studioCursor = response.cursor,
                                studios = if (isLoadMore) it.studios + response.studios else response.studios,
                                hasStudioMoreData = if (isLoadMore) response.count > (it.studios + response.studios).size else response.count > response.studios.size,
                            )
                        }
                    },
                    onFailure = {
                        // TODO
                        _state.update {
                            it.copy(
                                uiState = if (isLoadMore) UiState.Success else UiState.Error,
                                isLoading = false,
                                isMoreDataLoading = false
                            )
                        }
                    }
                )
        }
    }

    private fun onSearchClicked() {
        viewModelScope.launch(Dispatchers.Main) {
            val newKeyword = _state.value.searchKeyword.text.toString()

            if (newKeyword.isBlank()) {
                globalSnackbarManager.showSnackbar(
                    SnackBarData(
                        text = UiText.StringResource(R.string.snackbar_please_keyword)
                    )
                )
                return@launch
            }

            if (newKeyword == _state.value.keyword) return@launch

            searchRepository.saveSearchKeyword(newKeyword)

            _state.update {
                it.copy(
                    isLoading = true,
                    keyword = newKeyword,
                    hasAnimeMoreData = true,
                    hasActorMoreData = true,
                    hasStudioMoreData = true,
                    animeCursor = null,
                    animeCount = 0,
                    animes = emptyList(),
                    actorCursor = null,
                    actorCount = 0,
                    actors = emptyList(),
                    studioCursor = null,
                    studioCount = 0,
                    studios = emptyList(),
                )
            }

            loadAnime()
            loadActor()
            loadStudio()
        }
    }

    private fun onSubmitAnimeLog(logUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            animeRepository.submitAnimeLog(logUrl)
        }
    }

    private fun retry() {
        _state.update { it.copy(uiState = UiState.Loading) }

        loadAnime()
        loadActor()
        loadStudio()
    }
}