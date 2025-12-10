package com.jparkbro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickScrimColor
import com.jparkbro.ui.theme.AniPickWhite

@Composable
fun APFullScreenLoading(
    modifier: Modifier = Modifier,
    message: String? = null,
    backgroundColor: Color = AniPickScrimColor
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .pointerInput(Unit) { },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_huge)),
                color = AniPickPrimary,
                strokeWidth = dimensionResource(R.dimen.border_width_large)
            )

            message?.let {
                Text(
                    text = message,
                    style = AniPick14Normal.copy(color = AniPickWhite)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun APFullScreenLoadingPreview() {
    APFullScreenLoading()
}

@Preview(showBackground = true)
@Composable
private fun APFullScreenLoadingWithMessagePreview() {
    APFullScreenLoading(
        message = "로딩 중..."
    )
}
