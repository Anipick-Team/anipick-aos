package com.jparkbro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickWhite
import kotlin.math.max

/**
 * 애니메이션 용
 */
@Composable
fun APAnimeCard(
    modifier: Modifier = Modifier,
    cardWidth: Dp = 128.dp,
    imageUrl: String? = null,
    rank: Int? = null,
    title: String? = null,
    isSmallTitle: Boolean = false,
    maxLine: Int = 2,
    description: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .width(cardWidth)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
    ) {
        Box {
            rank?.let {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(AniPickPrimary, RoundedCornerShape(topStart = dimensionResource(R.dimen.corner_radius_small)))
                        .zIndex(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it.toString(),
                        style = AniPick14Normal.copy(color = AniPickWhite, fontWeight = FontWeight.W900)
                    )
                }
            }
            AsyncImage(
                model = imageUrl,
                contentDescription = stringResource(R.string.anime_cover_img),
                error = painterResource(R.drawable.thumbnail_img),
                modifier = Modifier
                    .width(cardWidth)
                    .clip(AniPickSmallShape)
                    .aspectRatio(2f/3f),
                contentScale = ContentScale.Crop
            )
        }
        title?.let {
            Text(
                text = it,
                style = if (isSmallTitle) AniPick14Normal else AniPick16Normal,
                color = AniPickBlack,
                maxLines = maxLine,
                minLines = maxLine,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .width(cardWidth)
            )
        }
        description?.let {
            it()
        }
    }
}

/**
 * 성우/캐릭터 용
 */
@Composable
fun APCastCard(
    modifier: Modifier = Modifier
) {
    Column(

    ) {

    }
}


@Preview(showBackground = true)
@Composable
fun APAnimeCardPreview() {
    APAnimeCard(
        rank = 1,
        title = "방패용사 성공담"
    )
}