package com.jparkbro.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APMainTopAppBar
import com.jparkbro.ui.components.AnimeSkeleton
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ChevronDownIcon
import com.jparkbro.ui.theme.ShimmerEffect
import com.jparkbro.ui.util.rememberGridInfo

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
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) {
                    Row(
                        modifier = Modifier
                            .height(36.dp)
                            .background(ShimmerEffect(), CircleShape)
                            .padding(horizontal = dimensionResource(R.dimen.padding_default)),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "년도",
                            style = AniPick16Normal.copy(color = Color.Transparent),
                        )
                        Icon(
                            imageVector = ChevronDownIcon,
                            contentDescription = stringResource(R.string.chevron_down_icon),
                            tint = Color.Transparent,
                            modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium))
                        )
                    }
                }
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickSurface
            )
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val horizontalPadding = dimensionResource(R.dimen.padding_large)
                val spacing = 8.dp

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
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.spacing_extra_large)
                    ),
                    contentPadding = PaddingValues(
                        start = horizontalPadding,
                        end = horizontalPadding,
                        top = dimensionResource(R.dimen.spacing_large)
                    )
                ) {
                    item(span = { GridItemSpan(gridInfo.columns) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(56.dp)
                                    .background(ShimmerEffect())
                            )
                        }
                    }
                    items(40) {
                        AnimeSkeleton(width = gridInfo.itemWidth)
                    }
                }
            }
        }
    }
}