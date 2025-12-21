package com.jparkbro.home.detail.components

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
import com.jparkbro.home.detail.HomeDetailState
import com.jparkbro.model.enum.HomeDetailType
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.HomeBannerMascotImg

@Composable
internal fun RecommendationBanner(
    state: HomeDetailState
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
            when (state.type) {
                HomeDetailType.RECOMMENDS -> {
                    if (state.referenceAnimeTitle == null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.home_main_recommended_prefix),
                                style = AniPick20Bold.copy(color = AniPickWhite)
                            )
                            Text(
                                text = state.nickname,
                                style = AniPick20Bold.copy(color = AniPickWhite),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Text(
                                text = stringResource(R.string.home_main_recommended_mid),
                                style = AniPick20Bold.copy(color = AniPickWhite)
                            )
                        }
                        Text(
                            text = stringResource(R.string.home_main_recommended_suffix),
                            style = AniPick20Bold.copy(color = AniPickWhite)
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = state.referenceAnimeTitle,
                                style = AniPick20Bold.copy(color = AniPickWhite),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Text(
                                text = stringResource(R.string.home_main_recommended_anime_prefix),
                                style = AniPick20Bold.copy(color = AniPickWhite)
                            )
                        }
                        Text(
                            text = stringResource(R.string.home_main_recommended_anime_suffix),
                            style = AniPick20Bold.copy(color = AniPickWhite)
                        )
                    }
                }
                HomeDetailType.SIMILAR_TO_WATCHED -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.home_main_similar_to_watched_prefix),
                            style = AniPick20Bold.copy(color = AniPickWhite)
                        )
                        state.referenceAnimeTitle?.let {
                            Text(
                                text = it,
                                style = AniPick20Bold.copy(color = AniPickWhite),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                        }
                        Text(
                            text = stringResource(R.string.home_main_similar_to_watched_mid),
                            style = AniPick20Bold.copy(color = AniPickWhite)
                        )
                    }
                    Text(
                        text = stringResource(R.string.home_main_similar_to_watched_suffix),
                        style = AniPick20Bold.copy(color = AniPickWhite)
                    )
                }
                else -> Unit
            }
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