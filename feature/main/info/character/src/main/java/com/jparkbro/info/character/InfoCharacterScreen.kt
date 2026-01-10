package com.jparkbro.info.character

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.info.character.components.SkeletonScreen
import com.jparkbro.model.common.UiState
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APCastPairCard
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APTitleTopAppBar
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.util.rememberGridInfo
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun InfoCharacterRoot(
    onNavigateBack: () -> Unit,
    onNavigateToActor: (Long) -> Unit,
    viewModel: InfoCharacterViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                onAction = { action ->
                    when (action) {
                        InfoCharacterAction.NavigateBack -> onNavigateBack()
                    }
                }
            )
        }

        UiState.Error -> {
            APErrorScreen(
                onClick = { viewModel.onAction(InfoCharacterAction.OnRetryClicked) }
            )
        }

        UiState.Success -> {
            InfoCharacterScreen(
                state = state,
                onAction = { action ->
                    when (action) {
                        InfoCharacterAction.NavigateBack -> onNavigateBack()
                        is InfoCharacterAction.NavigateToActor -> onNavigateToActor(action.actorId)
                    }
                    viewModel.onAction(action)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InfoCharacterScreen(
    state: InfoCharacterState,
    onAction: (InfoCharacterAction) -> Unit,
) {
    val listState = rememberLazyGridState()

    LaunchedEffect(listState, state.casts.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 6
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !state.isLoading && state.casts.isNotEmpty() && state.hasMoreData) {
                    onAction(InfoCharacterAction.LoadMoreCasts)
                }
            }
    }

    Scaffold(
        topBar = {
            APTitleTopAppBar(
                title = stringResource(R.string.info_series_header),
                onNavigateBack = { onAction(InfoCharacterAction.NavigateBack) },
            )
        },
        containerColor = AniPickSurface
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AniPickWhite)
        ) {
            val horizontalPadding = dimensionResource(R.dimen.padding_large)
            val spacing = 8.dp
            val defaultSingleItemWidth = 100.dp
            val defaultPairCardWidth = (defaultSingleItemWidth * 2) + spacing

            val gridInfo = rememberGridInfo(
                availableWidth = maxWidth,
                horizontalPadding = horizontalPadding * 2,
                spacing = spacing,
                defaultItemWidth = defaultPairCardWidth,
                minColumns = 2,
                maxColumns = 4
            )
            val actualItemWidth = (gridInfo.itemWidth - spacing) / 2

            Log.d("InfoCharacterScreen", "maxWidth: $maxWidth, columns: ${gridInfo.columns}, gridInfo.itemWidth: ${gridInfo.itemWidth}, actualItemWidth: $actualItemWidth")

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = dimensionResource(R.dimen.border_width_default),
                    color = AniPickGray100
                )
                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Fixed(gridInfo.columns),
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.spacing_default)
                    ),
                    contentPadding = PaddingValues(
                        horizontal = horizontalPadding,
                        vertical = dimensionResource(R.dimen.spacing_extra_large)
                    )
                ) {
                    items(state.casts) { cast ->
                        APCastPairCard(
                            cast = cast,
                            cardWidth = actualItemWidth,
                            onClick = { onAction(InfoCharacterAction.NavigateToActor(cast.voiceActor?.id ?: 0)) }
                        )
                    }

                    item(span = { GridItemSpan(gridInfo.columns) }) {
                        if (state.isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = dimensionResource(R.dimen.padding_default)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = AniPickPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}