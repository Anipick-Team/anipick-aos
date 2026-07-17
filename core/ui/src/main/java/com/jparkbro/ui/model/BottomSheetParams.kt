package com.jparkbro.ui.model

import com.jparkbro.model.common.ResponseMap

data class BottomSheetParams(
    val year: String? = null,
    val quarter: ResponseMap = ResponseMap(id = -1, name = "전체분기"),
    val genres: List<ResponseMap> = emptyList(),
    val isMatchAllConditions: Boolean = false,
    val type: String? = null,
)