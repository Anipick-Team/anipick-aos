package com.jparkbro.preferencesetup

import com.jparkbro.ui.model.BottomSheetData

interface PreferenceSetupEvent {
    data class InitLoadFailed(val message: String) : PreferenceSetupEvent
    data class AnimeLoadFailed(val message: String) : PreferenceSetupEvent
    data object SubmitSuccess : PreferenceSetupEvent
    data class SubmitFailed(val message: String) : PreferenceSetupEvent
    data class ShowBottomSheet(val data: BottomSheetData) : PreferenceSetupEvent
}