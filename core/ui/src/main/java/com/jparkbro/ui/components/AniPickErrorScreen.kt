package com.jparkbro.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.jparkbro.ui.R
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick18Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.EmptyImage1
import com.jparkbro.ui.theme.EmptyImage2
import com.jparkbro.ui.theme.EmptyImage3
import kotlin.random.Random

@Composable
fun APErrorScreen(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val randomNumber = remember { Random.nextInt(1, 4) } // 1, 2, 3 중 랜덤
    val randomImageResource: ImageVector = when (randomNumber) {
        1 -> EmptyImage1
        2 -> EmptyImage2
        else -> EmptyImage3
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large))
        ) {
            Image(
                imageVector = randomImageResource,
                contentDescription = stringResource(R.string.error_image)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
            ) {
                Text(
                    text = stringResource(R.string.network_error_title),
                    style = AniPick18Bold.copy(color = AniPickBlack),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.network_error_subtitle),
                    style = AniPick16Normal.copy(color = AniPickGray400),
                    textAlign = TextAlign.Center
                )
            }
        }
        APPrimaryActionButton(
            text = stringResource(R.string.retry_btn),
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(dimensionResource(R.dimen.padding_extra_large))
        )
    }
}

@DevicePreviews
@Composable
private fun APErrorScreenPreview() {
    APErrorScreen(
        onClick = {}
    )
}