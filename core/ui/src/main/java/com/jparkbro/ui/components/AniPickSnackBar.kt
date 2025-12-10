package com.jparkbro.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.util.UiText
import com.jparkbro.ui.model.SnackBarData
import kotlinx.coroutines.delay

@Composable
fun APSnackBarRe(
    snackBarData: SnackBarData,
) {
    val visibleState = remember(snackBarData) {
        MutableTransitionState(false)
    }

    LaunchedEffect(snackBarData) {
        visibleState.targetState = true
        delay(snackBarData.duration)
        visibleState.targetState = false
        delay(300)
        snackBarData.onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
    ) {
        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(tween(300)) + slideInVertically(
                initialOffsetY = { -it },
                animationSpec = tween(300)
            ),
            exit = fadeOut(tween(300)) + slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = tween(300)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_default))
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + dimensionResource(R.dimen.padding_default))
                .align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AniPickBlack, AniPickSmallShape)
                    .padding(dimensionResource(R.dimen.padding_medium)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = snackBarData.text.asString(),
                    style = AniPick14Normal.copy(AniPickGray50),
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 100)
@Composable
private fun APSnackBarPreview() {
    APSnackBarRe(
        snackBarData = SnackBarData(
            text = UiText.DynamicString("전송이 완료되었어요."),
            duration = 2000
        )
    )
}

