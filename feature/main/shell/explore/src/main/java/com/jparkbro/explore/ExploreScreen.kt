package com.jparkbro.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.explore.components.SelectedFilter
import com.jparkbro.explore.components.SkeletonScreen
import com.jparkbro.explore.components.SortButton
import com.jparkbro.model.common.MetaData
import com.jparkbro.model.common.UiState
import com.jparkbro.model.enum.BottomSheetType
import com.jparkbro.model.enum.RankingType
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APAnimeCard
import com.jparkbro.ui.components.APEmptyContent
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APFilterBottomSheet
import com.jparkbro.ui.components.APFilterTriggerChip
import com.jparkbro.ui.components.APMainTopAppBar
import com.jparkbro.ui.model.BottomSheetData
import com.jparkbro.ui.model.BottomSheetParams
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.util.ObserveAsEvents
import com.jparkbro.ui.util.rememberGridInfo
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExploreRoot(
    metaData: MetaData,
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    var bottomSheetData by rememberSaveable { mutableStateOf<BottomSheetData?>(null) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ExploreEvent.ShowBottomSheet -> {
                bottomSheetData = event.data.copy(
                    onDismiss = { bottomSheetData = null },
                    onConfirm = { params ->
                        viewModel.onAction(ExploreAction.OnBottomSheetCompleteClicked(params))
                    }
                )
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                bottomNav = bottomNav,
                onNavigateToSearch = onNavigateToSearch,
            )
        }
        UiState.Error -> {
            Scaffold(
                topBar = { APMainTopAppBar(onNavigateToSearch = onNavigateToSearch) },
                bottomBar = bottomNav,
                containerColor = AniPickWhite
            ) {
                APErrorScreen(
                    onClick = { viewModel.onAction(ExploreAction.OnRetryClicked) },
                    modifier = Modifier.padding(it)
                )
            }
        }
        UiState.Success -> {
            ExploreScreen(
                bottomNav = bottomNav,
                state = state,
                onAction = { action ->
                    when (action) {
                        ExploreAction.NavigateToSearch -> onNavigateToSearch()
                        is ExploreAction.NavigateToInfoAnime -> onNavigateToInfoAnime(action.animeId)
                    }
                    viewModel.onAction(action)
                }
            )
        }
    }

    bottomSheetData?.let { data ->
        APFilterBottomSheet(
            metaData = metaData,
            initData = data.params,
            includeYearQuarter = data.includeYearQuarter,
            includeGenres = data.includeGenres,
            allowMultipleSelection = data.allowMultipleSelection,
            includeTypeFilter = data.includeTypeFilter,
            initSheetType = data.sheetType,
            onDismiss = data.onDismiss,
            onApply = { data.onConfirm(it) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExploreScreen(
    bottomNav: @Composable () -> Unit,
    state: ExploreState,
    onAction: (ExploreAction) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.isLoading) {
        if (state.isLoading) {
            listState.scrollToItem(0)
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.canScrollForward }
            .distinctUntilChanged()
            .collect { canScrollForward ->
                if (!canScrollForward && !state.isMoreDataLoading && state.animes.isNotEmpty() && state.hasMoreData) {
                    onAction(ExploreAction.OnLoadMore)
                }
            }
    }

    Scaffold(
        topBar = { APMainTopAppBar(onNavigateToSearch = { onAction(ExploreAction.NavigateToSearch) }) },
        bottomBar = bottomNav,
        containerColor = AniPickWhite
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            stickyHeader {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = dimensionResource(R.dimen.border_width_default),
                    color = AniPickSurface
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_large)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    APFilterTriggerChip(
                        title = stringResource(R.string.explore_chip_year_quarter),
                        isSelected = state.year != "전체년도" && state.year != null,
                        onClick = { onAction(ExploreAction.OnFilterChipClicked(BottomSheetType.YEAR_QUARTER)) }
                    )
                    APFilterTriggerChip(
                        title = stringResource(R.string.explore_chip_genre),
                        isSelected = state.genres.isNotEmpty(),
                        onClick = { onAction(ExploreAction.OnFilterChipClicked(BottomSheetType.GENRE)) }
                    )
                    APFilterTriggerChip(
                        title = stringResource(R.string.explore_chip_type),
                        isSelected = state.type != null,
                        onClick = { onAction(ExploreAction.OnFilterChipClicked(BottomSheetType.TYPE)) }
                    )
                }
            }
            stickyHeader {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = dimensionResource(R.dimen.border_width_default),
                    color = AniPickSurface
                )
                if ((state.year != "전체년도" && state.year != null) || state.genres.isNotEmpty() || state.type != null) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AniPickWhite)
                            .padding(vertical = dimensionResource(R.dimen.padding_default)),
                        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large)),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
                    ) {
                        state.year?.let {
                            if (it != "전체년도") {
                                item {
                                    SelectedFilter(
                                        title = it,
                                        onClick = { onAction(ExploreAction.OnYearFilterCancelClicked) }
                                    )
                                }
                            }
                        }
                        state.quarter.name?.let {
                            if (it != "전체분기") {
                                item {
                                    SelectedFilter(
                                        title = it,
                                        onClick = { onAction(ExploreAction.OnQuarterFilterCancelClicked) }
                                    )
                                }
                            }
                        }
                        if (state.genres.isNotEmpty()) {
                            items(state.genres) { genre ->
                                genre.name?.let {
                                    SelectedFilter(
                                        title = it,
                                        onClick = { onAction(ExploreAction.OnGenreFilterCancelClicked(genre.id)) }
                                    )
                                }
                            }
                        }
                        state.type?.let {
                            item {
                                SelectedFilter(
                                    title = it,
                                    onClick = { onAction(ExploreAction.OnTypeFilterCancelClicked) }
                                )
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = dimensionResource(R.dimen.border_width_default),
                        color = AniPickSurface
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AniPickWhite)
                        .padding(horizontal = dimensionResource(R.dimen.padding_large), vertical = dimensionResource(R.dimen.padding_default)),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    SortButton(
                        state = state,
                        onAction = onAction
                    )
                }
            }

            if (state.animes.isNotEmpty()) {
                item {
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

                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = horizontalPadding)
                                .padding(bottom = dimensionResource(R.dimen.padding_extra_large)),
                            horizontalArrangement = Arrangement.spacedBy(spacing - 1.dp),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                            maxItemsInEachRow = gridInfo.columns
                        ) {
                            state.animes.forEach { anime ->
                                APAnimeCard(
                                    cardWidth = gridInfo.itemWidth,
                                    imageUrl = anime.coverImageUrl,
                                    title = anime.title,
                                    isSmallTitle = true,
                                    maxLine = 2,
                                    description = {
                                        anime.airDate?.let {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth(),
                                                contentAlignment = Alignment.CenterStart
                                            ) {
                                                Text(
                                                    text = it,
                                                    style = AniPick12Normal.copy(color = AniPickGray400),
                                                )
                                            }
                                        }
                                    },
                                    onClick = { onAction(ExploreAction.NavigateToInfoAnime(anime.animeId ?: -1)) }
                                )
                            }
                        }
                    }
                }
                if (state.isMoreDataLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = dimensionResource(R.dimen.padding_default)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = AniPickPrimary)
                        }
                    }
                }
            } else {
                if (!state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillParentMaxHeight(0.7f),
                            contentAlignment = Alignment.Center
                        ) {
                            APEmptyContent(comment = stringResource(R.string.empty_content))
                        }
                    }
                }
            }
        }
    }
}