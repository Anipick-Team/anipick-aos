package com.jparkbro.ui.util

import androidx.compose.ui.unit.Dp
import com.jparkbro.ui.model.GridInfo

fun rememberGridInfo(
    availableWidth: Dp,
    horizontalPadding: Dp,
    spacing: Dp,
    defaultItemWidth: Dp,
    minColumns: Int,
    maxColumns: Int = Int.MAX_VALUE
): GridInfo {

    val usableWidth = availableWidth - horizontalPadding

    val columns = ((usableWidth + spacing) /
            (defaultItemWidth + spacing))
        .toInt()
        .coerceIn(minColumns, maxColumns)

    // 항상 실제 화면 넓이에 맞춰 재계산하여 빈 공간 최소화
    val itemWidth = (usableWidth - spacing * (columns - 1)) / columns

    return GridInfo(
        columns = columns,
        itemWidth = itemWidth
    )
}
