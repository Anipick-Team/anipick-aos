package jpark.bro.preferencesetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jpark.bro.ui.FilterParams
import jpark.bro.ui.FilterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.logging.Filter
import javax.inject.Inject

@HiltViewModel
class PreferenceSetupViewModel @Inject constructor(

) : ViewModel() {

    private val _searchText = MutableStateFlow<String>("")
    val searchText: StateFlow<String> = _searchText

    private val _yearFilter = MutableStateFlow<String>("전체년도")
    val yearFilter: StateFlow<String> = _yearFilter

    private val _quarterFilter = MutableStateFlow<String>("전체분기")
    val quarterFilter: StateFlow<String> = _quarterFilter

    private val _genreFilter = MutableStateFlow<String>("")
    val genreFilter: StateFlow<String> = _genreFilter

    private val _showBottomSheet = MutableStateFlow<Boolean>(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet

    private val _filterType = MutableStateFlow<FilterType>(FilterType.YEAR_AND_QUARTER)
    val filterType: StateFlow<FilterType> = _filterType

    fun updateSearch(search: String) {
        _searchText.value = search
    }

    fun updateShowBottomSheet(isVisible: Boolean) {
        _showBottomSheet.value = isVisible
    }

    fun updateFilterType(type: FilterType) {
        _filterType.value = type
    }

    // 검색어로 조회
    fun searchByKeyword() {
        viewModelScope.launch {

        }
    }

    // 필터로 조회
    fun searchByFilters(params: FilterParams) {
        _yearFilter.value = params.year
        _quarterFilter.value = params.quarter
        _genreFilter.value = if (params.genres.isEmpty()) {
            ""
        } else {
            params.genres[0]
        }

        // API 호출
        viewModelScope.launch {

        }
    }

    // 조회된 애니메이션 아이템 리스트

    // 사용자가 평가한 애니메이션 리스트

    // 완료(평가종료) API

}