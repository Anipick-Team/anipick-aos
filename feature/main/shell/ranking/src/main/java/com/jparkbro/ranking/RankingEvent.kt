package com.jparkbro.ranking

import com.jparkbro.ui.model.BottomSheetData

interface RankingEvent {
    data class ShowBottomSheet(val data: BottomSheetData) : RankingEvent
}