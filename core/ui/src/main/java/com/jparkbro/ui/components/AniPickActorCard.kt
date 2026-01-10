package com.jparkbro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jparkbro.model.common.actor.Cast
import com.jparkbro.model.common.actor.Filmography
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface

/**
 * 성우 and 캐릭터
 */
@Composable
fun APCastPairCard(
    cast: Cast,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 100.dp,
    maxLine: Int = 2,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .background(AniPickSurface, AniPickSmallShape)
            .clip(AniPickSmallShape)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
    ) {
        Column(
            modifier = Modifier
                .width(cardWidth)
        ) {
            AsyncImage(
                model = cast.character?.imageUrl?.takeIf { !it.contains("default.jpg") },
                contentDescription = stringResource(R.string.character_img),
                error = painterResource(R.drawable.thumbnail_img),
                placeholder = painterResource(R.drawable.thumbnail_img),
                modifier = Modifier
                    .width(cardWidth)
                    .aspectRatio(3f/4f),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .width(cardWidth)
                    .height(42.dp)
                    .padding(start = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                cast.character?.name?.let {
                    Text(
                        text = it,
                        style = AniPick14Normal.copy(color = AniPickBlack),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = maxLine,
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .width(cardWidth)
        ) {
            AsyncImage(
                model = cast.voiceActor?.imageUrl?.takeIf { !it.contains("default.jpg") },
                contentDescription = stringResource(R.string.actor_img),
                error = painterResource(R.drawable.thumbnail_img),
                placeholder = painterResource(R.drawable.thumbnail_img),
                modifier = Modifier
                    .width(cardWidth)
                    .aspectRatio(3f/4f),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .width(cardWidth)
                    .height(42.dp)
                    .padding(start = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                cast.voiceActor?.name?.let {
                    Text(
                        text = it,
                        style = AniPick14Normal.copy(color = AniPickBlack),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = maxLine,
                    )
                }
            }
        }
    }
}

/**
 * 성우 or 캐릭터 단일
 */
@Composable
fun APCastCard(
    modifier: Modifier = Modifier,
    cardWidth: Dp = 100.dp,
    work: Filmography,
    onClick: (Long) -> Unit = {},
) {
    Column(
        modifier = modifier
            .width(cardWidth)
            .clickable { onClick(work.animeId ?: 0) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
    ) {
        Box {
            AsyncImage(
                model = work.characterImageUrl?.takeIf { !it.contains("default.jpg") },
                contentDescription = stringResource(R.string.character_img),
                error = painterResource(R.drawable.thumbnail_img),
                placeholder = painterResource(R.drawable.thumbnail_img),
                modifier = Modifier
                    .width(cardWidth)
                    .clip(AniPickSmallShape)
                    .aspectRatio(3f/4f),
                contentScale = ContentScale.Crop
            )
        }
        work.characterName?.let {
            Text(
                text = it,
                style = AniPick14Normal.copy(color = AniPickBlack),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(cardWidth)
            )
        }
        work.animeTitle?.let {
            Text(
                text = it,
                style = AniPick12Normal.copy(color = AniPickGray400),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(cardWidth)
            )
        }
    }
}