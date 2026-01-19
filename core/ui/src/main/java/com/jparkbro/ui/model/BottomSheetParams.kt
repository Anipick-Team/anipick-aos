package com.jparkbro.ui.model

import com.jparkbro.model.common.ResponseMap

data class BottomSheetParams(
    val year: String = "전체년도",
    val quarter: ResponseMap = ResponseMap(name = "전체분기"),
    val genres: List<ResponseMap> = emptyList(),
    val isMatchAllConditions: Boolean = false,
    val type: String = "",
)
