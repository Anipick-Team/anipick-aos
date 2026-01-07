package com.jparkbro.info.series.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jparkbro.info.series.InfoSeriesAction
import com.jparkbro.info.series.InfoSeriesState
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APTitleTopAppBar
import com.jparkbro.ui.components.AnimeSkeleton
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.util.rememberGridInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SkeletonScreen(
    state: InfoSeriesState,
    onAction: (InfoSeriesAction) -> Unit
) {
    val horizontalPadding = dimensionResource(R.dimen.padding_large)
    val spacing = 8.dp

    Scaffold(
        topBar = {
            APTitleTopAppBar(
                title = stringResource(R.string.info_series_header),
                onNavigateBack = { onAction(InfoSeriesAction.NavigateBack) },
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
                color = AniPickGray100
            )
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
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.spacing_extra_large)
                    ),
                    contentPadding = PaddingValues(
                        start = horizontalPadding,
                        end = horizontalPadding,
                        top = dimensionResource(R.dimen.spacing_extra_large)
                    )
                ) {
                    items(40) {
                        AnimeSkeleton(width = gridInfo.itemWidth)
                    }
                }
            }
        }
    }
}