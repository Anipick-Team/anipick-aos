package com.jparkbro.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.search.SearchRepository
import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.search.SearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    private val _popularAnimes = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val popularAnimes: StateFlow<SearchUiState> = _popularAnimes.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    fun updateSearchText(query: String) {
        _searchText.value = query
    }

    init {
        initPage()
    }

    private fun initPage() {
        viewModelScope.launch {
            _popularAnimes.value = SearchUiState.Loading

            loadRecentSearchKeywords()

            // 인기 애니 조회 (Api)
            searchRepository.getPopularAnimes().fold(
                onSuccess = {
                    _popularAnimes.value = SearchUiState.Success(it.popularAnimes)
                },
                onFailure = {
                    // TODO
                }
            )
        }
    }

    fun loadRecentSearchKeywords() {
        viewModelScope.launch {
            // 최근 검색어 조회 (DataStore)
            searchRepository.loadSearchKeyword().fold(
                onSuccess = {
                    _recentSearches.value = it
                },
                onFailure = {
                    // TODO
                }
            )
        }
    }

    fun saveRecentSearch(query: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            searchRepository.saveSearchKeyword(query).fold(
                onSuccess = {
                    onResult(true)
                },
                onFailure = {
                    // TODO
                }
            )
        }
    }

    fun deleteRecentSearch(query: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            searchRepository.deleteSearchKeyword(query).fold(
                onSuccess = {
                    onResult(true)
                },
                onFailure = {
                    // TODO
                }
            )
        }
    }

    fun deleteAllRecentSearches(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            searchRepository.deleteAll().fold(
                onSuccess = {
                    onResult(true)
                },
                onFailure = {
                    // TODO
                }
            )
        }
    }
}

sealed interface SearchUiState {
    data object Loading: SearchUiState
    data class Success(val data: List<DefaultAnime>): SearchUiState
    data class Error(val msg: String): SearchUiState
}