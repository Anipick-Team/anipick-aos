package com.jparkbro.explore

import com.jparkbro.ui.model.BottomSheetData

interface ExploreEvent {
    data class ShowBottomSheet(val data: BottomSheetData) : ExploreEvent
}