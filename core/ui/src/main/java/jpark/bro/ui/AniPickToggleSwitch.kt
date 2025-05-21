package jpark.bro.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun APToggleSwitch(
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 상수 정의
    val width = 32
    val height = 16
    val dotSize = 12
    val paddingHorizontal = 2

    val maxOffset = width - dotSize - (paddingHorizontal * 2)

    val backgroundColor by animateColorAsState(
        targetValue = if (checked) Color(0xFF667080) else Color(0x4D667080),
        animationSpec = tween(durationMillis = 100),
        label = "backgroundColor"
    )
    val offsetX by animateFloatAsState(
        targetValue = if (checked) maxOffset.toFloat() else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "offsetX"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable { onCheckedChange() }
            .padding(4.dp)
            .clip(RoundedCornerShape(50))
            .size(width = width.dp, height = height.dp)
            .background(backgroundColor),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(start = paddingHorizontal.dp)
                .size(dotSize.dp)
                .offset(x = offsetX.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White)
        )
    }
}