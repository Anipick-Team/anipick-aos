package com.jparkbro.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.ranking.RankingRepository
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.ranking.RankingItem
import com.jparkbro.model.ranking.RankingRequest
import com.jparkbro.model.ranking.RankingResponse
import com.jparkbro.model.ranking.RankingType
import com.jparkbro.ui.FilterParams
import com.jparkbro.ui.FilterType
import com.jparkbro.ui.SheetData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val rankingRepository: RankingRepository
) : ViewModel() {

    private val _response = MutableStateFlow<RankingResponse?>(null)
    val response = _response.asStateFlow()

    private val _animeList = MutableStateFlow<List<RankingItem>>(emptyList())
    val animeList = _animeList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _hasMoreData = MutableStateFlow(true)

    private val _params = MutableStateFlow(RankingRequest(type = RankingType.REAL_TIME, genre = ResponseMap()))
    val params: StateFlow<RankingRequest> = _params.asStateFlow()

    private val _bottomSheetData = MutableStateFlow<SheetData?>(null)
    val bottomSheetData: StateFlow<SheetData?> = _bottomSheetData.asStateFlow()

    fun updateBottomSheetData(data: SheetData? = null) {
        _bottomSheetData.value = data
    }

    init {
        getAnimesRank()
    }

    fun updateFilter(request: RankingRequest) {
        _response.value = null
        _params.value = createRankingParams(request)
        getAnimesRank()
    }

    private fun createRankingParams(request: RankingRequest): RankingRequest {
        val baseParams = _params.value.copy(
            type = request.type,
            genre = request.genre ?: _params.value.genre
        )

        return when (request.type) {
            RankingType.YEAR_SEASON -> baseParams.copy(
                year = sanitizeYear(request.year),
                season = sanitizeSeason(request.year, request.season)
            )
            else -> baseParams.copy(
                year = null,
                season = null
            )
        }
    }

    fun getAnimesRank() {
        if (_isLoading.value || !_hasMoreData.value) return

        _isLoading.value = true

        if (_response.value?.cursor?.lastId == null) {
            _animeList.value = emptyList()
        }

        viewModelScope.launch {
            rankingRepository.getAnimesRank(
                request = _params.value.copy(
                    lastId = _animeList.value.lastOrNull()?.popularity,
                    lastValue = _animeList.value.lastOrNull()?.trending,
                    lastRank = _animeList.value.lastOrNull()?.rank,
                )
            ).fold(
                onSuccess = {
                    _response.value = it
                    _animeList.value += it.animes

                    _isLoading.value = false
                    _hasMoreData.value = it.animes.size == 20
                },
                onFailure = {
                    _isLoading.value = false
                    _hasMoreData.value = true
                }
            )
        }
    }

    private fun sanitizeYear(year: String?): String? {
        return when {
            year == null -> null
            year == "전체년도" -> null
            year.isBlank() -> null
            else -> year
        }
    }

    private fun sanitizeSeason(year: String?, season: String?): String? {
        return when {
            year == null || year == "전체년도" -> null
            season == null -> null
            season == "전체분기" -> null
            season.isBlank() -> null
            else -> season
        }
    }
}