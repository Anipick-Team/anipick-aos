package com.jparkbro.preferencesetup

import com.jparkbro.model.dto.preference.RatedAnime
import com.jparkbro.model.enum.BottomSheetType
import com.jparkbro.ui.model.BottomSheetParams

interface PreferenceSetupAction {
    data object OnSkipClicked : PreferenceSetupAction
    data object OnSearchClicked : PreferenceSetupAction
    data object OnClearTextClicked : PreferenceSetupAction
    data class OnFilterChipClicked(val type: BottomSheetType) : PreferenceSetupAction
    data object OnClearFilterClicked : PreferenceSetupAction
    data class OnFilterCompleteClicked(val params: BottomSheetParams) : PreferenceSetupAction
    data class OnRatingAddClicked(val rate: RatedAnime) : PreferenceSetupAction
    data class OnRatingRemoveClicked(val animeId: Long) : PreferenceSetupAction
    data object OnCompleteClicked : PreferenceSetupAction
    data object OnLoadMore : PreferenceSetupAction
    data object OnRetryClicked : PreferenceSetupAction
}