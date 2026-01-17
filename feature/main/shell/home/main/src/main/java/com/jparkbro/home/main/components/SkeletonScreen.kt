package com.jparkbro.home.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APMainTopAppBar
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ShimmerEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SkeletonScreen(
    bottomNav: @Composable () -> Unit,
) {
    Scaffold(
        topBar = { APMainTopAppBar(onNavigateToSearch = {}) },
        bottomBar = bottomNav,
        containerColor = AniPickSurface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickGray100
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
            ) {
                repeat(6) {
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
                                text = stringResource(R.string.home_main_upcoming_release),
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
}