package com.jparkbro.info.recommend.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jparkbro.info.recommend.InfoRecommendState
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.HomeBannerMascotImg

@Composable
internal fun Banner(
    state: InfoRecommendState
)  {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF010000), AniPickSmallShape)
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_large))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "'${state.bannerTitle}'",
                    style = AniPick20Bold.copy(color = AniPickWhite),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = stringResource(R.string.info_recommend_banner_mid),
                    style = AniPick20Bold.copy(color = AniPickWhite)
                )
            }
            Text(
                text = stringResource(R.string.info_recommend_banner_suffix),
                style = AniPick20Bold.copy(color = AniPickWhite)
            )
        }
        Image(
            imageVector = HomeBannerMascotImg,
            contentDescription = stringResource(R.string.anime_banner_img),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(top = 76.dp, end = dimensionResource(R.dimen.padding_extra_large))
        )
    }
}