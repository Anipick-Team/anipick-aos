package com.jparkbro.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jparkbro.ui.R
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick18Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.EmptyImage1
import com.jparkbro.ui.theme.EmptyImage2
import com.jparkbro.ui.theme.EmptyImage3
import kotlin.random.Random

@Composable
fun APEmptyContent(
    modifier: Modifier = Modifier,
    comment: String,
) {
    val randomNumber = remember { Random.nextInt(1, 4) } // 1, 2, 3 중 랜덤
    val randomImageResource: ImageVector = when (randomNumber) {
        1 -> EmptyImage1
        2 -> EmptyImage2
        else -> EmptyImage3
    }
    Box(
        modifier = modifier
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
            Text(
                text = comment,
                style = AniPick16Normal.copy(color = AniPickGray400),
                textAlign = TextAlign.Center
            )
        }
    }
}

@DevicePreviews
@Composable
private fun APEmptyContentPreview() {
    APEmptyContent(
        comment = "검색하신 조건에 맞는 컨텐츠가 없습니다."
    )
}