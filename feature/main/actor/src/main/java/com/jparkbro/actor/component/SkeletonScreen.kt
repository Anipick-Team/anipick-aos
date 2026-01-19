package com.jparkbro.actor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jparkbro.actor.ActorAction
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APTitleTopAppBar
import com.jparkbro.ui.components.AnimeSkeleton
import com.jparkbro.ui.components.PersonSkeleton
import com.jparkbro.ui.theme.AniPickExtraSmallShape
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ShimmerEffect
import com.jparkbro.ui.util.rememberGridInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SkeletonScreen(
    onAction: (ActorAction) -> Unit
) {
    val horizontalPadding = dimensionResource(R.dimen.padding_large)
    val spacing = 8.dp

    Scaffold(
        topBar = {
            APTitleTopAppBar(
                title = stringResource(R.string.info_series_header),
                onNavigateBack = { onAction(ActorAction.NavigateBack) },
            )
        },
        containerColor = AniPickWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(3f/4f)
                        .clip(AniPickSmallShape)
                        .background(ShimmerEffect())
                )
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(24.dp)
                        .background(ShimmerEffect())
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = dimensionResource(R.dimen.border_width_large),
                color = AniPickSurface
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_large))
                    .padding(top = dimensionResource(R.dimen.padding_large), bottom = dimensionResource(R.dimen.padding_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large)),
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(24.dp)
                        .background(ShimmerEffect())
                )
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(16.dp)
                        .background(ShimmerEffect())
                )
            }
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val gridInfo = rememberGridInfo(
                    availableWidth = maxWidth,
                    horizontalPadding = horizontalPadding * 2,
                    spacing = spacing,
                    defaultItemWidth = 128.dp,
                    minColumns = 3,
                    maxColumns = 5
                )

                LazyVerticalGrid(
                    userScrollEnabled = false,
                    columns = GridCells.Fixed(gridInfo.columns),
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large)),
                    contentPadding = PaddingValues(
                        start = horizontalPadding,
                        end = horizontalPadding,
                    )
                ) {
                    items(40) {
                        PersonSkeleton(width = gridInfo.itemWidth)
                    }
                }
            }
        }
    }
}