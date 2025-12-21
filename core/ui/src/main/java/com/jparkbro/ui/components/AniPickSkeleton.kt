package com.jparkbro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick20Normal
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ShimmerEffect

@Composable
fun ReviewSkeleton() {
    Column(
        modifier = Modifier
            .background(AniPickWhite)
            .padding(
                horizontal = dimensionResource(R.dimen.padding_large),
                vertical = dimensionResource(R.dimen.padding_default)
            ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(116.dp)
                    .aspectRatio(2f/3f)
                    .clip(AniPickSmallShape)
                    .background(ShimmerEffect())
            )
            Text(
                text = "철권: 블러드레인철권: ",
                style = AniPick16Normal.copy(color = Color.Transparent),
                modifier = Modifier
                    .background(ShimmerEffect())
            )
        }
        HorizontalDivider()
        Column{
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "철권: 블러드레인철권: ",
                    style = AniPick20Normal.copy(color = Color.Transparent),
                    modifier = Modifier
                        .background(ShimmerEffect())
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(ShimmerEffect())
                    )
                    Text(
                        text = "Jpark",
                        style = AniPick14Normal.copy(color = Color.Transparent),
                        modifier = Modifier
                            .background(ShimmerEffect())
                    )
                }
            }
            Text(
                text = "철권: 블러드레인철권: ",
                style = AniPick12Normal.copy(color = Color.Transparent),
                modifier = Modifier
                    .background(ShimmerEffect())
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))
            Text(
                text = "철권: 블러드레인철권: ",
                style = AniPick16Normal.copy(color = Color.Transparent),
                modifier = Modifier
                    .background(ShimmerEffect())
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(20.dp)
                    .background(ShimmerEffect())
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(ShimmerEffect())
            )
        }
    }
}

@Composable
fun AnimeSkeleton(
    width: Dp = 128.dp
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .aspectRatio(2f/3f)
                .clip(AniPickSmallShape)
                .background(ShimmerEffect())
        )
        Box(
            modifier = Modifier
                .width(width)
                .height(20.dp)
                .background(ShimmerEffect())
        )
    }
}

@Preview
@Composable
private fun ReviewSkeletonPreview() {
    ReviewSkeleton()
}

@Preview
@Composable
private fun AnimeSkeletonPreview() {
    AnimeSkeleton()
}