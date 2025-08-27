package com.jparkbro.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.search.SearchRepository
import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.search.SearchRequest
import com.jparkbro.model.search.SearchResultAnime
import com.jparkbro.model.search.SearchResultPerson
import com.jparkbro.model.search.SearchResultResponse
import com.jparkbro.model.search.SearchResultStudio
import com.jparkbro.model.search.SearchType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SearchResultViewModel.Factory::class)
class SearchResultViewModel @AssistedInject constructor(
    private val searchRepository: SearchRepository,
    @Assisted val searchParam: String,
) : ViewModel() {

    private val _searchText = MutableStateFlow(searchParam)
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _searchType = MutableStateFlow(SearchType.ANIMES)
    val searchType: StateFlow<SearchType> = _searchType.asStateFlow()

    private val _uiState = MutableStateFlow<SearchResultUiState>(SearchResultUiState.Loading)
    val uiState: StateFlow<SearchResultUiState> = _uiState.asStateFlow()

    fun updateSearchText(query: String) {
        _searchText.value = query
    }

    fun updateSearchType(type: SearchType) {
        _searchType.value = type
        loadSearchResults()
    }

    private val _response = MutableStateFlow<SearchResultResponse?>(null)
    val response = _response.asStateFlow()

    private val _animeList = MutableStateFlow<List<SearchResultAnime>>(emptyList())

    private val _personList = MutableStateFlow<List<SearchResultPerson>>(emptyList())

    private val _studioList = MutableStateFlow<List<SearchResultStudio>>(emptyList())

    val currentDataList: StateFlow<List<Any>> = combine(
        _searchType,
        _animeList,
        _personList,
        _studioList
    ) { searchType, animes, persons, studios ->
        when (searchType) {
            SearchType.ANIMES -> animes
            SearchType.PERSONS -> persons
            SearchType.STUDIOS -> studios
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var _hasMoreData = mutableStateOf(true)


    init {
        loadSearchResults()
    }

    fun loadSearchResults(lastId: Int? = null, page: Int? = null) {
        val isInitialLoad = lastId == null

        if (lastId != null && (_isLoading.value || !_hasMoreData.value)) return

        if (lastId == null) {
            _response.value = null
            _hasMoreData.value = true
            when (_searchType.value) {
                SearchType.ANIMES -> _animeList.value = emptyList()
                SearchType.PERSONS -> _personList.value = emptyList()
                SearchType.STUDIOS -> _studioList.value = emptyList()
            }
        }

        _isLoading.value = true

        viewModelScope.launch {
            if (isInitialLoad) _uiState.value = SearchResultUiState.Loading

            searchRepository.getSearchResult(
                type = _searchType.value,
                request = SearchRequest(
                    query = _searchText.value,
                    lastId = lastId,
                    page = page,
                    size = if (_searchType.value == SearchType.STUDIOS) 25 else 18
                )
            ).fold(
                onSuccess = {
                    if (isInitialLoad) _uiState.value = SearchResultUiState.Success

                    _isLoading.value = false
                    _response.value = it

                    when (_searchType.value) {
                        SearchType.ANIMES -> {
                            _animeList.value += it.animes
                            it.animes.size == 18
                        }
                        SearchType.PERSONS -> {
                            _personList.value += it.persons
                            it.persons.size == 18
                        }
                        SearchType.STUDIOS -> {
                            _studioList.value += it.studios
                            it.studios.size == 25
                        }
                    }
                },
                onFailure = {
                    if (isInitialLoad) _uiState.value = SearchResultUiState.Error("${it.message}")
                    _isLoading.value = false
                }
            )
        }
    }

    fun saveRecentSearch(query: String) {
        viewModelScope.launch {
            searchRepository.saveSearchKeyword(query)
            // TODO
        }
    }

    fun submitAnimeLog(logUrl: String) {
        viewModelScope.launch {
            searchRepository.submitAnimeLog(logUrl).getOrThrow()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            searchParam: String
        ): SearchResultViewModel
    }
}

sealed interface SearchResultUiState {
    data object Loading: SearchResultUiState
    data object Success: SearchResultUiState
    data class Error(val msg: String): SearchResultUiState
}