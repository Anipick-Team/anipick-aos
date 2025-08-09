package com.jparkbro.preferencesetup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.AuthRepository
import com.jparkbro.model.auth.PreferenceRequest
import com.jparkbro.model.auth.PreferenceResponse
import com.jparkbro.model.auth.RatedAnime
import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.ui.FilterParams
import com.jparkbro.ui.SheetData
import com.jparkbro.ui.util.extension.quarterStringToInt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferenceSetupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _bottomSheetData = MutableStateFlow<SheetData?>(null)
    val bottomSheetData: StateFlow<SheetData?> = _bottomSheetData.asStateFlow()

    fun updateBottomSheetData(data: SheetData? = null) {
        _bottomSheetData.value = data
    }

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _yearFilter = MutableStateFlow("전체년도")
    val yearFilter: StateFlow<String> = _yearFilter.asStateFlow()

    private val _quarterFilter = MutableStateFlow("전체분기")
    val quarterFilter: StateFlow<String> = _quarterFilter.asStateFlow()

    private val _genreFilter = MutableStateFlow(ResponseMap())
    val genreFilter: StateFlow<ResponseMap> = _genreFilter.asStateFlow()

    fun updateSearch(search: String) {
        _searchText.value = search
    }

    private val _searchResponse = MutableStateFlow<PreferenceResponse?>(null)
    val searchResponse: StateFlow<PreferenceResponse?> = _searchResponse.asStateFlow()

    private val _animes = MutableStateFlow<List<DefaultAnime>>(emptyList())
    val animes = _animes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 더 불러올 데이터 있는지
    private val _hasMoreData = MutableStateFlow(true)

    init {
        loadAnimeData()
    }

    fun loadAnimeData(lastId: Int? = null) {
        if (lastId != null && (_isLoading.value || !_hasMoreData.value)) return

        _isLoading.value = true

        viewModelScope.launch {
            if (lastId == null) _animes.value = emptyList()

            authRepository.exploreOrSearch(
                request = PreferenceRequest(
                    query = _searchText.value,
                    year = if (_yearFilter.value == "전체년도") null else _yearFilter.value,
                    season = _quarterFilter.value.quarterStringToInt(),
                    genres = if (_genreFilter.value.id == -1) null else _genreFilter.value.id,
                    lastId = lastId,
                    size = 10
                )
            ).fold(
                onSuccess = {

                    when (it.animes.size) {
                        10 -> {
                            _animes.value += it.animes
                            _hasMoreData.value = true
                        }
                        0 -> {
                            _hasMoreData.value = false
                        }
                        else -> {
                            _animes.value += it.animes
                            _hasMoreData.value = false
                        }
                    }
                    _searchResponse.value = it
                    _isLoading.value = false
                },
                onFailure = {
                    _isLoading.value = false
                }
            )
        }
    }

    fun searchByFilters(params: FilterParams) {
        _yearFilter.value = params.year
        _quarterFilter.value = params.quarter
        _genreFilter.value = if (params.genres.isEmpty()) {
            ResponseMap()
        } else {
            params.genres[0]
        }

        loadAnimeData()
    }

    // 사용자가 평가한 애니메이션 리스트
    private val _ratedAnimes = MutableStateFlow<List<RatedAnime>>(emptyList())
    val ratedAnime: StateFlow<List<RatedAnime>> = _ratedAnimes.asStateFlow()

    fun addRateAnime(rate: RatedAnime) {
        val currentList = _ratedAnimes.value.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.animeId == rate.animeId }

        if (existingIndex != -1) {
            currentList[existingIndex] = rate
        } else {
            currentList.add(rate)
        }

        _ratedAnimes.value = currentList
    }

    fun removeRateAnime(animeId: Int) {
        _ratedAnimes.value = _ratedAnimes.value.filter { it.animeId != animeId }
    }

    // 완료(평가종료) API
    fun submitReviews(skip: Boolean) {
        viewModelScope.launch {
            if (skip) {
                authRepository.submitReviews(request = emptyList())
            } else {
                authRepository.submitReviews(request = _ratedAnimes.value)
            }

            // TODO 반환 결과 처리 필요 ( 성공 후 페이지 이동 ? )
        }
    }
}