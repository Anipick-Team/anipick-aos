package com.jparkbro.search.main

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.data.search.SearchRepository
import com.jparkbro.model.common.UiState
import com.jparkbro.ui.R
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.snackbar.GlobalSnackbarManager
import com.jparkbro.ui.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val globalSnackbarManager: GlobalSnackbarManager,
    private val animeRepository: AnimeRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<SearchEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        loadAnime()
        observeRecentKeywords()
    }

    private fun observeRecentKeywords() {
        searchRepository.searchKeywords
            .onEach { keywords ->
                _state.update { it.copy(recentKeywords = keywords) }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SearchAction) {
        when (action) {
            SearchAction.OnRetryClicked -> retry()
            SearchAction.OnClearSearchKeyword -> _state.update { it.copy(searchKeyword = TextFieldState()) }
            SearchAction.OnSearchClicked -> onSearch()
            is SearchAction.OnDeleteSearchKeyword -> onDeleteSearchKeyword(action.keyword)
            SearchAction.OnDeleteAllSearchKeywords -> onDeleteAllSearchKeywords()
        }
    }

    private fun loadAnime() {
        viewModelScope.launch(Dispatchers.IO) {
            animeRepository.getPopularAnimes()
                .fold(
                    onSuccess = { animes ->
                        _state.update {
                            it.copy(
                                uiState = UiState.Success,
                                animes = animes
                            )
                        }
                    },
                    onFailure = { _state.update { it.copy(uiState = UiState.Error) } }
                )
        }
    }

    private fun onSearch() {
        viewModelScope.launch(Dispatchers.IO) {
            val keyword = _state.value.searchKeyword.text.toString()
            if (keyword.isNotBlank()) {
                searchRepository.saveSearchKeyword(keyword)
                _eventChannel.send(SearchEvent.SearchSuccess(keyword))
            } else {
                globalSnackbarManager.showSnackbar(
                    SnackBarData(
                        text = UiText.StringResource(R.string.snackbar_please_keyword)
                    )
                )
            }
        }
    }

    private fun onDeleteSearchKeyword(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchRepository.deleteSearchKeyword(keyword)
        }
    }

    private fun onDeleteAllSearchKeywords() {
        viewModelScope.launch(Dispatchers.IO) {
            searchRepository.deleteAll()
        }
    }

    private fun retry() {
        _state.update { it.copy(uiState = UiState.Loading) }
        loadAnime()
    }

}
