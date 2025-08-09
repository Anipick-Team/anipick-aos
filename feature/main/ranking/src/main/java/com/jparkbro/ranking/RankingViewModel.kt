package com.jparkbro.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.ranking.RankingRepository
import com.jparkbro.model.common.ResponseMap
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

    private val _params = MutableStateFlow(RankingRequest(type = RankingType.REAL_TIME, genre = ResponseMap()))
    val params: StateFlow<RankingRequest> = _params.asStateFlow()

    private val _uiState = MutableStateFlow<RankingUiState>(RankingUiState.Loading)
    val uiState: StateFlow<RankingUiState> = _uiState.asStateFlow()

    private val _bottomSheetData = MutableStateFlow<SheetData?>(null)
    val bottomSheetData: StateFlow<SheetData?> = _bottomSheetData.asStateFlow()

    fun updateBottomSheetData(data: SheetData? = null) {
        _bottomSheetData.value = data
    }

    init {
        getAnimesRank(RankingRequest(type = RankingType.REAL_TIME))
    }

    fun updateFilter(request: RankingRequest) {
        val newParams = when (request.type) {
            RankingType.YEAR_SEASON -> {
                _params.value.copy(
                    type = request.type,
                    year = sanitizeYear(request.year),
                    season = sanitizeSeason(request.year, request.season),
                    genre = request.genre ?: _params.value.genre,
                    lastId = null,
                    size = null
                )
            }
            else -> {
                _params.value.copy(
                    type = request.type,
                    year = null,
                    season = null,
                    genre = request.genre ?: _params.value.genre,
                    lastId = null,
                    size = null
                )
            }
        }
        _params.value = newParams

        getAnimesRank(_params.value)
    }

    private fun getAnimesRank(request: RankingRequest) {
        viewModelScope.launch {
            _uiState.value = RankingUiState.Loading

            rankingRepository.getAnimesRank(request).fold(
                onSuccess = {
                    _uiState.value = RankingUiState.Success(it)
                },
                onFailure = {
                    // TODO
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

sealed interface RankingUiState {
    data object Loading: RankingUiState
    data class Success(val item: RankingResponse): RankingUiState
    data class Error(val msg: String): RankingUiState
}