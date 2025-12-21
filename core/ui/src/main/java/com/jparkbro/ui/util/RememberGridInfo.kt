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

    val itemWidth = when {
        columns == minColumns || columns == maxColumns -> {
            (usableWidth - spacing * (columns - 1)) / columns
        }
        else -> defaultItemWidth
    }

    return GridInfo(
        columns = columns,
        itemWidth = itemWidth
    )
}
