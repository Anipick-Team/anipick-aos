package com.jparkbro.preferencesetup.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.model.dto.preference.PreferenceAnime
import com.jparkbro.model.dto.preference.RatedAnime
import com.jparkbro.ui.R
import com.jparkbro.ui.components.DraggableStarRating
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickExtraSmallShape
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickPoint
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ShimmerEffect
import com.jparkbro.ui.theme.StarFillIcon
import com.jparkbro.ui.theme.StarOutlineIcon

@Composable
internal fun AnimeRatingCard(
    initRating: Float,
    anime: PreferenceAnime,
    onRatingAdd: (RatedAnime) -> Unit = {},
    onRatingRemove: (Int) -> Unit = {},
) {
    var rating by rememberSaveable(initRating) { mutableFloatStateOf(initRating) }
    var showRating by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = dimensionResource(R.dimen.border_width_default),
                    color = AniPickSurface,
                    shape = AniPickSmallShape
                )
                .clip(AniPickSmallShape)
                .clickable { showRating = !showRating }
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = anime.coverImageUrl,
                contentDescription = stringResource(R.string.anime_cover_img),
                error = painterResource(R.drawable.thumbnail_img),
                modifier = Modifier
                    .size(width = 132.dp, height = 88.dp)
                    .clip(AniPickSmallShape),
                contentScale = ContentScale.Crop,
                alignment = if (anime.coverImageUrl != null) Alignment.Center else Alignment.BottomCenter
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = anime.title,
                            style = AniPick16Normal.copy(color = AniPickBlack),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        if (initRating != 0f) {
                            Box(
                                modifier = Modifier
                                    .clip(AniPickExtraSmallShape)
                                    .background(color = AniPickPrimary)
                                    .clickable { onRatingRemove(anime.animeId) }
                                    .padding(
                                        horizontal = dimensionResource(R.dimen.padding_small),
                                        vertical = dimensionResource(R.dimen.padding_extra_small)
                                    )
                            ) {
                                Text(
                                    text = stringResource(R.string.preference_setup_cancel),
                                    style = AniPick12Normal,
                                    color = AniPickWhite
                                )
                            }
                        }
                    }
                    if (anime.genres.isNotEmpty()) {
                        Text(
                            text = anime.genres.joinToString(", "),
                            style = AniPick14Normal.copy(color = AniPickGray400)
                        )
                    }
                }
                if (initRating != 0f) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            for (i in 0 until 5) {
                                Box(
                                    modifier = Modifier
                                        .size(dimensionResource(R.dimen.icon_size_medium))
                                ) {
                                    Image(
                                        imageVector = StarOutlineIcon,
                                        contentDescription = stringResource(R.string.outline_star_icon),
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                    when {
                                        i < initRating.toInt() -> {
                                            Image(
                                                imageVector = StarFillIcon,
                                                contentDescription = stringResource(R.string.fill_star_icon),
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }

                                        i == initRating.toInt() && initRating % 1 != 0f -> {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clipToBounds()
                                                    .drawWithContent {
                                                        // 왼쪽 반쪽만 클리핑
                                                        clipRect(left = 0f, top = 0f, right = size.width / 2, bottom = size.height) {
                                                            this@drawWithContent.drawContent()
                                                        }
                                                    }
                                            ) {
                                                Image(
                                                    imageVector = StarFillIcon,
                                                    contentDescription = stringResource(R.string.fill_star_icon),
                                                    modifier = Modifier.fillMaxSize(),
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Text(
                            text = stringResource(R.string.preference_setup_rating_complete, initRating),
                            style = AniPick14Normal.copy(color = AniPickPoint)
                        )
                    }
                }
            }
        }

        if (showRating) {
            Row(
                modifier = Modifier
                    .clip(AniPickSmallShape)
                    .background(AniPickSurface)
                    .padding(
                        horizontal = dimensionResource(R.dimen.padding_large),
                        vertical = dimensionResource(R.dimen.padding_medium)
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DraggableStarRating(
                        starSize = 28.dp,
                        initialRating = rating,
                        onRatingChanged = { rating = it },
                    )
                    Text(
                        text = stringResource(R.string.preference_setup_rating_format, rating),
                        style = AniPick16Normal,
                        color = if (rating != 0f) AniPickPoint else AniPickGray100
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(AniPickSmallShape)
                        .background(color = if (rating != 0f) AniPickPrimary else AniPickGray100)
                        .clickable(
                            enabled = rating != 0f
                        ) {
                            onRatingAdd(
                                RatedAnime(
                                    animeId = anime.animeId,
                                    rating = rating
                                )
                            )
                            showRating = false
                        }
                        .padding(
                            horizontal = dimensionResource(R.dimen.padding_default),
                            vertical = dimensionResource(R.dimen.padding_medium)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.preference_setup_rating),
                        style = AniPick12Normal,
                        color = AniPickWhite
                    )
                }
            }
        }
    }
}

@Composable
internal fun AnimeRatingCardSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = dimensionResource(R.dimen.border_width_default),
                    color = AniPickSurface,
                    shape = AniPickSmallShape
                )
                .clip(AniPickSmallShape)
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 132.dp, height = 88.dp)
                    .clip(AniPickSmallShape)
                    .background(brush = ShimmerEffect())
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "애니메이션 제목",
                            style = AniPick16Normal.copy(color = Color.Transparent),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .background(brush = ShimmerEffect())
                        )

                    }
                    Text(
                        text = "드라마, 공포, 스릴러",
                        style = AniPick14Normal.copy(color = Color.Transparent),
                        modifier = Modifier
                            .background(brush = ShimmerEffect())
                    )
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun AnimeRatingCardPreview() {
    AnimeRatingCard(
        initRating = 3.5f,
        anime = PreferenceAnime(
            title = "애니메이션 제목",
            genres = listOf("드라마", "공포", "스릴러")
        ),
        onRatingAdd = {},
        onRatingRemove = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AnimeRatingCardSkeletonPreview() {
    AnimeRatingCardSkeleton()
}