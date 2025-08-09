package com.jparkbro.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun calculateItemSpacing(
    itemWidth: Dp = 115.dp,
    itemsPerRow: Int = 3,
    horizontalPadding: Dp = 20.dp
): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val availableWidth = screenWidth - (horizontalPadding * 2)
    val totalItemWidth = itemWidth * itemsPerRow
    return ((availableWidth - totalItemWidth) / (itemsPerRow - 1)) - 1.dp
}