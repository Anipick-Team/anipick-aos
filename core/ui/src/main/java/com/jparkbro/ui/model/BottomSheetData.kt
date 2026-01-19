package com.jparkbro.ui.model

import com.jparkbro.model.enum.BottomSheetType

data class BottomSheetData(
    val sheetType: BottomSheetType,
    val params: BottomSheetParams,
    val onDismiss: () -> Unit = {},
    val onConfirm: (BottomSheetParams) -> Unit = {},
)
