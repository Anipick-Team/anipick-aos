package com.jparkbro.ranking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.model.common.anime.Anime
import com.jparkbro.model.enum.RankingTrend
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickPoint
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSecondary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.RankingDownIcon
import com.jparkbro.ui.theme.RankingHoldIcon
import com.jparkbro.ui.theme.RankingUpIcon

@Composable
internal fun RankingItem(
    anime: Anime,
    showRankChange: Boolean = false,
    onClick: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick(anime.animeId ?: 0) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .width(40.dp),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small))
        ) {
            anime.rank?.let {
                Text(
                    text = it.toString().padStart(2, '0'),
                    style = AniPick20Bold.copy(color = AniPickBlack),
                )
            }
            when (anime.trend) {
                RankingTrend.UP, RankingTrend.DOWN, RankingTrend.SAME -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when (anime.trend) {
                                RankingTrend.UP -> RankingUpIcon
                                RankingTrend.DOWN -> RankingDownIcon
                                else -> RankingHoldIcon
                            },
                            contentDescription = stringResource(R.string.rank_change_icon),
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.icon_size_extra_small)),
                            tint = when (anime.trend) {
                                RankingTrend.UP -> AniPickPoint
                                RankingTrend.DOWN -> AniPickSecondary
                                else -> AniPickGray400
                            }
                        )
                        if (showRankChange) {
                            anime.change?.let {
                                Text(
                                    text = if (anime.change == "N") stringResource(R.string.ranking_rank_new) else it,
                                    style = AniPick14Normal.copy(
                                        color = when (anime.trend) {
                                            RankingTrend.UP -> AniPickPoint
                                            RankingTrend.DOWN -> AniPickSecondary
                                            else -> AniPickGray400
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
                else -> {
                    if (showRankChange) {
                        Text(
                            text = stringResource(R.string.ranking_rank_new),
                            style = AniPick14Normal.copy(color = AniPickPrimary)
                        )
                    }
                }
            }
        }
        AsyncImage(
            model = anime.coverImageUrl?.takeIf { !it.contains("default.jpg") },
            contentDescription = stringResource(R.string.anime_cover_img),
            error = painterResource(R.drawable.thumbnail_img),
            placeholder = painterResource(R.drawable.thumbnail_img),
            modifier = Modifier
                .width(128.dp)
                .aspectRatio(2f/3f)
                .clip(AniPickSmallShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_default)))
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small))
        ) {
            anime.title?.let {
                Text(
                    text = it,
                    style = AniPick16Normal.copy(color = AniPickBlack),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
            ) {
                anime.genres.forEach { genre ->
                    genre?.let {
                        Text(
                            text = it,
                            style = AniPick12Normal.copy(color = AniPickPrimary),
                            modifier = Modifier
                                .background(AniPickPrimary.copy(alpha = 0.1f), AniPickSmallShape)
                                .padding(horizontal = dimensionResource(R.dimen.padding_small), vertical = dimensionResource(R.dimen.padding_extra_small))
                        )
                    }
                }
            }
        }
    }
}