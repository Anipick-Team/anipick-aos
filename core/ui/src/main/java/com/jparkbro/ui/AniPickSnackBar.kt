package com.jparkbro.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.jparkbro.ui.theme.APColors
import kotlinx.coroutines.delay

@Composable
fun APSnackBar(
    snackBarData: SnackBarData,
    visible: Boolean = false,
    onDismiss: () -> Unit = {},
) {
    val visibleState = remember(visible) {
        MutableTransitionState(false).apply {
            targetState = visible
        }
    }

    LaunchedEffect(visible) {
        if (visible) {
            delay(snackBarData.duration)
            onDismiss()
        }
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
                .padding(horizontal = 16.dp)
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp)
                .align(Alignment.TopCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(APColors.Black, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = snackBarData.text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.LightGray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun APSnackBarPreview() {
    APSnackBar(
        snackBarData = SnackBarData(
            text = "전송이 완료되었어요.",
            duration = 2000
        ),
        visible = true
    )
}

data class SnackBarData(
    val text: String = "",
    val duration: Long = 2000,
)