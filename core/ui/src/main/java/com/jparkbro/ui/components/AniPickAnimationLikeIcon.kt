package com.jparkbro.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.FavoriteOffIcon
import com.jparkbro.ui.theme.FavoriteOnIcon

@Composable
fun APAnimationLikeIcon(
    size: Dp = dimensionResource(R.dimen.icon_size_medium),
    isLiked: Boolean = false,
    isLikingAnime: Boolean = false,
    onClick: (Boolean) -> Unit,
) {
    var targetScale by rememberSaveable { mutableFloatStateOf(1f) }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "likeScale"
    )

    var previousIsLiked by rememberSaveable { mutableStateOf(isLiked) }

    LaunchedEffect(isLiked) {
        if (!previousIsLiked && isLiked) {
            targetScale = 1.5f
            kotlinx.coroutines.delay(150)
            targetScale = 1f
        }
        previousIsLiked = isLiked
    }

    Icon(
        imageVector = if (isLiked) FavoriteOnIcon else FavoriteOffIcon,
        contentDescription = stringResource(R.string.favorite_icon),
        tint = Color.Unspecified,
        modifier = Modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                enabled = !isLikingAnime,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick(isLiked)
            }
    )
}