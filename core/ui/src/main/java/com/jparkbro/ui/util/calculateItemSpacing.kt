package com.jparkbro.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun calculateCardWidth(
    maxWidth: Dp = 115.dp,
    itemsPerRow: Int = 3,
    horizontalPadding: Dp = 20.dp,
    itemSpacing: Dp = 8.dp
): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val availableWidth = screenWidth - (horizontalPadding * 2)
    val totalSpacing = itemSpacing * (itemsPerRow - 1)
    val calculatedWidth = (availableWidth - totalSpacing) / itemsPerRow
    return if (calculatedWidth < maxWidth) calculatedWidth else maxWidth
}

@Composable
fun calculateItemSpacing(
    itemWidth: Dp = 115.dp,
    itemsPerRow: Int = 3,
    horizontalPadding: Dp = 20.dp,
    minSpacing: Dp = 8.dp
): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val availableWidth = screenWidth - (horizontalPadding * 2)
    val totalItemWidth = itemWidth * itemsPerRow
    val calculatedSpacing = (availableWidth - totalItemWidth) / (itemsPerRow - 1)

    return if (calculatedSpacing < minSpacing) minSpacing else calculatedSpacing
}

