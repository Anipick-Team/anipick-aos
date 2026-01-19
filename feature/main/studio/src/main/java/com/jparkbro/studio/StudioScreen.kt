package com.jparkbro.studio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.model.common.UiState
import com.jparkbro.studio.components.SkeletonScreen
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APAnimeCard
import com.jparkbro.ui.components.APCardItem
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APTitleTopAppBar
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.util.calculateCardWidth
import com.jparkbro.ui.util.calculateItemSpacing
import com.jparkbro.ui.util.rememberGridInfo
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun StudioRoot(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    viewModel: StudioViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                onAction = { action ->
                    when (action) {
                        StudioAction.NavigateBack -> onNavigateBack()
                    }
                }
            )
        }

        UiState.Error -> {
            APErrorScreen(
                onClick = { viewModel.onAction(StudioAction.OnRetryClicked) }
            )
        }

        UiState.Success -> {
            StudioScreen(
                state = state,
                onAction = { action ->
                    when (action) {
                        StudioAction.NavigateBack -> onNavigateBack()
                        is StudioAction.NavigateToInfoAnime -> onNavigateToInfoAnime(action.animeId)
                    }
                    viewModel.onAction(action)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudioScreen(
    state: StudioState,
    onAction: (StudioAction) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, state.animes.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 6
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !state.isLoading && state.animes.isNotEmpty() && state.hasMoreData) {
                    onAction(StudioAction.LoadMoreAnime)
                }
            }
    }

    Scaffold(
        topBar = {
            APTitleTopAppBar(
                title = state.studioName ?: stringResource(R.string.studio_header),
                onNavigateBack = { onAction(StudioAction.NavigateBack) },
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

            val gridInfo = rememberGridInfo(
                availableWidth = maxWidth,
                horizontalPadding = horizontalPadding * 2,
                spacing = spacing,
                defaultItemWidth = 128.dp,
                minColumns = 3,
                maxColumns = 5
            )

            val groupByYear = state.animes
                .groupBy { it.seasonYear }
                .toSortedMap(reverseOrder())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AniPickSurface)
            ) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = dimensionResource(R.dimen.border_width_default),
                    color = AniPickGray100
                )
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                ) {
                    groupByYear.forEach { (year, animeList) ->
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(AniPickWhite)
                                    .padding(horizontal = dimensionResource(R.dimen.padding_large), vertical = dimensionResource(R.dimen.padding_extra_large)),
                                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large))
                            ) {
                                Text(
                                    text = year,
                                    style = AniPick14Normal.copy(color = AniPickBlack),
                                    modifier = Modifier
                                        .background(color = AniPickGray50, CircleShape)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                )

                                FlowRow(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(spacing - 1.dp),
                                    verticalArrangement = Arrangement.spacedBy(spacing),
                                    maxItemsInEachRow = gridInfo.columns
                                ) {
                                    animeList.forEach { anime ->
                                        APAnimeCard(
                                            cardWidth = gridInfo.itemWidth,
                                            imageUrl = anime.coverImageUrl,
                                            title = anime.title,
                                            isSmallTitle = true,
                                            maxLine = 2,
                                            onClick = { onAction(StudioAction.NavigateToInfoAnime(anime.animeId ?: -1)) }
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
}