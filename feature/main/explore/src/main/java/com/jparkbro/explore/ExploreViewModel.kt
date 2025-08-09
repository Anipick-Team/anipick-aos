package com.jparkbro.explore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.explore.ExploreRepository
import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.explore.ExploreRequest
import com.jparkbro.model.explore.ExploreResponse
import com.jparkbro.model.explore.InitDate
import com.jparkbro.ui.FilterParams
import com.jparkbro.ui.FilterType
import com.jparkbro.ui.SheetData
import com.jparkbro.ui.util.extension.quarterStringToInt
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = ExploreViewModel.Factory::class)
class ExploreViewModel @AssistedInject constructor(
    private val exploreRepository: ExploreRepository,
    @Assisted val initDate: InitDate,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExploreUiState>(ExploreUiState.Loading)
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    private val _bottomSheetData = MutableStateFlow<SheetData?>(null)
    val bottomSheetData: StateFlow<SheetData?> = _bottomSheetData.asStateFlow()

    fun updateBottomSheetData(data: SheetData? = null) {
        _bottomSheetData.value = data
    }

    private val _sortType = MutableStateFlow("인기순")
    val sortType: StateFlow<String> = _sortType.asStateFlow()

    private val _showSortDropdown = MutableStateFlow(false)
    val showSortDropdown: StateFlow<Boolean> = _showSortDropdown.asStateFlow()

    fun updateSortType(type: String) {
        _sortType.value = type

        loadAnimes()
    }

    fun changeDropdownState() {
        _showSortDropdown.value = !_showSortDropdown.value
    }

    private val _yearFilter = MutableStateFlow(initDate.year ?: "전체년도")
    val yearFilter: StateFlow<String> = _yearFilter.asStateFlow()

    private val _quarterFilter = MutableStateFlow(initDate.quarter ?: "전체분기")
    val quarterFilter: StateFlow<String> = _quarterFilter.asStateFlow()

    private val _genreFilter = MutableStateFlow<List<ResponseMap>>(emptyList())
    val genreFilter: StateFlow<List<ResponseMap>> = _genreFilter.asStateFlow()

    private val _typeFilter = MutableStateFlow("")
    val typeFilter: StateFlow<String> = _typeFilter.asStateFlow()

    private val _isMatchAllConditions = MutableStateFlow(false)
    val isMatchAllConditions: StateFlow<Boolean> = _isMatchAllConditions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 더 불러올 데이터 있는지
    private val _hasMoreData = MutableStateFlow(true)

    private val _exploreResponse = MutableStateFlow<ExploreResponse?>(null)
    val exploreResponse = _exploreResponse.asStateFlow()

    private val _animes = MutableStateFlow<List<DefaultAnime>>(emptyList())
    val animes = _animes.asStateFlow()

    init {
        loadAnimes()
    }

    fun cancelFilter(type: CancelFilterType, item: Int? = null) {
        when (type) {
            CancelFilterType.YEAR -> {
                _yearFilter.value = "전체년도"
                _quarterFilter.value = "전체분기"
            }
            CancelFilterType.QUARTER -> _quarterFilter.value = "전체분기"
            CancelFilterType.TYPE -> _typeFilter.value = ""
            CancelFilterType.GENRE -> {
                _genreFilter.value = _genreFilter.value.filterNot { it.id == item }
            }
        }

        loadAnimes()
    }

    fun updateFilters(params: FilterParams) {
        _yearFilter.value = params.year
        _quarterFilter.value = params.quarter
        _genreFilter.value = params.genres
        _typeFilter.value = params.type
        _isMatchAllConditions.value = params.isMatchAllConditions

        loadAnimes()
    }

    fun loadAnimes(lastId: Int? = null) {
        val isInit = _animes.value.isEmpty()

        if (lastId == null) {
            _exploreResponse.value = null
            _animes.value = emptyList()
        }

        if (lastId != null && (_isLoading.value || !_hasMoreData.value)) return

        viewModelScope.launch {
            if (isInit) _uiState.value = ExploreUiState.Loading

            _isLoading.value = true

            exploreRepository.exploreAnime(
                request = ExploreRequest(
                    year = if (_yearFilter.value == "전체년도") null else _yearFilter.value,
                    season = _quarterFilter.value.quarterStringToInt(),
                    genres = if (_genreFilter.value.isNotEmpty()) _genreFilter.value.map { it.id } else null,
                    type = _typeFilter.value,
                    sort = if (_sortType.value == "인기순") "popularity" else "rating",
                    genreOp = if (_isMatchAllConditions.value) "AND" else "OR",
                    lastId = _exploreResponse.value?.cursor?.lastId,
                    lastValue = _exploreResponse.value?.cursor?.lastValue,
                    size = 18,
                )
            ).fold(
                onSuccess = {
                    if (isInit) _uiState.value = ExploreUiState.Success

                    _isLoading.value = false
                    _exploreResponse.value = it
                    _animes.value += it.animes
                    _hasMoreData.value = it.animes.size == 18
                },
                onFailure = {
                    _isLoading.value = false
                    // TODO
                }
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            initDate: InitDate
        ): ExploreViewModel
    }
}

enum class CancelFilterType {
    YEAR,
    QUARTER,
    GENRE,
    TYPE
}

sealed interface ExploreUiState {
    data object Loading: ExploreUiState
    data object Success: ExploreUiState
    data class Error(val msg: String): ExploreUiState
}