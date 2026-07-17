package com.jparkbro.ui.model

import com.jparkbro.model.enum.BottomSheetType

data class BottomSheetData(
    val sheetType: BottomSheetType,
    val params: BottomSheetParams,
    val includeYearQuarter: Boolean = false,
    val includeGenres: Boolean = false,
    val includeTypeFilter: Boolean = false,
    val allowMultipleSelection: Boolean = false,
    val onDismiss: () -> Unit = {},
    val onConfirm: (BottomSheetParams) -> Unit = {},
)
