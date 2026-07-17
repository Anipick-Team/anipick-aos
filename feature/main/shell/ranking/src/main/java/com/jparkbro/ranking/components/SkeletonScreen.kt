package com.jparkbro.ranking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APMainTopAppBar
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ShimmerEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SkeletonScreen(
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
) {
    Scaffold(
        topBar = { APMainTopAppBar(onNavigateToSearch = onNavigateToSearch) },
        bottomBar = bottomNav,
        containerColor = AniPickWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickSurface
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_large)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                ) {
                    repeat(3) {
                        Text(
                            text = "실시간",
                            style = AniPick16Normal.copy(color = Color.Transparent),
                            modifier = Modifier
                                .background(ShimmerEffect(), CircleShape)
                                .padding(horizontal = dimensionResource(R.dimen.padding_medium), vertical = dimensionResource(R.dimen.padding_small))
                        )
                    }
                }
                Text(
                    text = "미스테리",
                    style = AniPick16Normal.copy(color = Color.Transparent),
                    modifier = Modifier
                        .background(ShimmerEffect(), CircleShape)
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium), vertical = dimensionResource(R.dimen.padding_small))
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickSurface
            )
            LazyColumn(
                userScrollEnabled = false,
                contentPadding = PaddingValues(
                    horizontal = dimensionResource(R.dimen.padding_large), vertical = dimensionResource(R.dimen.padding_extra_large)
                ),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
            ) {
                items(6) {
                    SkeletonAnimeItem()
                }
            }
        }
    }
}

@Composable
internal fun SkeletonAnimeItem() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small))
        ) {
            Text(
                text = "01",
                style = AniPick20Bold.copy(color = Color.Transparent),
                modifier = Modifier
                    .widthIn(30.dp)
                    .background(ShimmerEffect())
            )
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(30.dp)
                    .background(ShimmerEffect())
            )
        }
        Box(
            modifier = Modifier
                .width(128.dp)
                .aspectRatio(2f/3f)
                .background(ShimmerEffect(), AniPickSmallShape)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small))
        ) {
            Text(
                text = "",
                style = AniPick16Normal.copy(color = Color.Transparent),
                minLines = 2,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ShimmerEffect())
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) {
                    Text(
                        text = "로맨스",
                        style = AniPick12Normal.copy(color = Color.Transparent),
                        modifier = Modifier
                            .background(ShimmerEffect(), AniPickSmallShape)
                            .padding(horizontal = dimensionResource(R.dimen.padding_small), vertical = dimensionResource(R.dimen.padding_extra_small))
                    )
                }
            }
        }
    }
}