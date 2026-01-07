package com.jparkbro.info.anime.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jparkbro.info.anime.InfoAnimeAction
import com.jparkbro.ui.R
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.BorderChevronLeftIcon
import com.jparkbro.ui.theme.ShimmerEffect

@Composable
internal fun SkeletonScreen(
    onAction: (InfoAnimeAction) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AniPickWhite)
            .windowInsetsPadding(
                WindowInsets(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
            ),
        userScrollEnabled = false
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(ShimmerEffect())
                )
                Icon(
                    imageVector = BorderChevronLeftIcon,
                    contentDescription = stringResource(R.string.back_stack_icon),
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(
                            top = dimensionResource(R.dimen.padding_extra_huge),
                            start = dimensionResource(R.dimen.padding_large)
                        )
                        .size(dimensionResource(R.dimen.icon_size_large))
                        .clip(CircleShape)
                        .clickable { onAction(InfoAnimeAction.NavigateBack) }
                )
                Box(
                    modifier = Modifier
                        .padding(
                            bottom = dimensionResource(R.dimen.padding_large),
                            end = dimensionResource(R.dimen.padding_large)
                        )
                        .width(124.dp)
                        .aspectRatio(2f / 3f)
                        .background(ShimmerEffect())
                        .align(Alignment.BottomEnd)
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_extra_large)))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_large))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "록은 숙녀의 소양이기에",
                            style = AniPick20Bold.copy(color = Color.Transparent),
                            modifier = Modifier
                                .background(ShimmerEffect())
                        )
                        Box(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.icon_size_medium))
                                .clip(AniPickSmallShape)
                                .background(ShimmerEffect())
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_extra_large))
                            .clip(AniPickSmallShape)
                            .background(ShimmerEffect())
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_medium))
                            .clip(AniPickSmallShape)
                            .background(ShimmerEffect())
                    )
                    Text(
                        text = "0.0",
                        style = AniPick20Bold.copy(color = Color.Transparent),
                        modifier = Modifier
                            .background(ShimmerEffect())
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_32)))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small))
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp)
                                .clip(AniPickSmallShape)
                                .background(ShimmerEffect())
                        )
                    }
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.spacing_extra_large)),
                thickness = dimensionResource(R.dimen.border_width_large),
                color = AniPickGray50
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_large))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .background(ShimmerEffect())
                )
                Text(
                    text = "작품설명",
                    style = AniPick14Normal.copy(color = Color.Transparent),
                    minLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.spacing_medium))
                        .background(ShimmerEffect())
                )
                Text(
                    text = "더보기 **",
                    style = AniPick14Normal.copy(color = Color.Transparent),
                    modifier = Modifier
                        .background(ShimmerEffect())
                )
            }
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.spacing_extra_large)),
                thickness = dimensionResource(R.dimen.border_width_large),
                color = AniPickGray50
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_large)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
            ) {
                repeat(6) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "방영 시기",
                            style = AniPick14Normal.copy(color = Color.Transparent),
                            modifier = Modifier
                                .background(ShimmerEffect())
                        )
                        Text(
                            text = "2024년 1분기",
                            style = AniPick14Normal.copy(color = Color.Transparent),
                            modifier = Modifier
                                .background(ShimmerEffect())
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
            repeat(3) {
                Column(
                    modifier = Modifier
                        .background(AniPickWhite)
                        .padding(vertical = dimensionResource(R.dimen.padding_huge)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.padding_large)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.home_main_popular_now),
                            style = AniPick20Bold.copy(color = Color.Transparent),
                            modifier = Modifier
                                .background(brush = ShimmerEffect())
                        )
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(brush = ShimmerEffect())
                        )
                    }
                    LazyRow(
                        userScrollEnabled = false,
                        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large)),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                    ) {
                        items(10) {
                            Column(
                                modifier = Modifier
                                    .width(128.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(128.dp)
                                        .aspectRatio(2f/3f)
                                        .clip(AniPickSmallShape)
                                        .background(brush = ShimmerEffect())
                                )
                                Text(
                                    text = "철권: 블러드라인",
                                    style = AniPick16Normal.copy(color = Color.Transparent),
                                    modifier = Modifier
                                        .background(brush = ShimmerEffect())
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun SkeletonScreenPreview() {
    SkeletonScreen({})
}