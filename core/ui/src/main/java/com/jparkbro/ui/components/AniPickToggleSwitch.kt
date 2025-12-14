package com.jparkbro.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPickGray500
import com.jparkbro.ui.theme.AniPickWhite

@Composable
fun APToggleSwitch(
    checked: Boolean,
    checkedColor: Color = AniPickGray500,
    unCheckedColor: Color = Color(0x4D667080),
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
        targetValue = if (checked) checkedColor else unCheckedColor,
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
            .clip(CircleShape)
            .clickable { onCheckedChange() }
            .padding(dimensionResource(R.dimen.padding_extra_small))
            .clip(CircleShape)
            .size(width = width.dp, height = height.dp)
            .background(backgroundColor),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(start = paddingHorizontal.dp)
                .size(dotSize.dp)
                .offset(x = offsetX.dp)
                .clip(CircleShape)
                .background(AniPickWhite)
        )
    }
}