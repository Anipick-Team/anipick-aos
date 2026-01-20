package com.jparkbro.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.model.common.MetaData
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.common.UiState
import com.jparkbro.model.dto.ranking.GetAnimeRankingRequest
import com.jparkbro.model.enum.BottomSheetType
import com.jparkbro.model.enum.RankingType
import com.jparkbro.model.ranking.RankingRequest
import com.jparkbro.ranking.components.FilterChip
import com.jparkbro.ranking.components.RankingItem
import com.jparkbro.ranking.components.SkeletonAnimeItem
import com.jparkbro.ranking.components.SkeletonScreen
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APFilterBottomSheet
import com.jparkbro.ui.components.APMainTopAppBar
import com.jparkbro.ui.components.FilterParams
import com.jparkbro.ui.components.FilterType
import com.jparkbro.ui.components.SheetData
import com.jparkbro.ui.model.BottomSheetData
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick16Regular
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSecondary
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ChevronDownIcon
import com.jparkbro.ui.util.ObserveAsEvents
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun RankingRoot(
    metaData: MetaData,
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    viewModel: RankingViewModel = hiltViewModel()
) {
    var bottomSheetData by rememberSaveable { mutableStateOf<BottomSheetData?>(null) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RankingEvent.ShowBottomSheet -> {
                bottomSheetData = event.data.copy(
                    onDismiss = { bottomSheetData = null },
                    onConfirm = { params ->
                        when (event.data.sheetType) {
                            BottomSheetType.YEAR_QUARTER -> viewModel.onAction(RankingAction.OnYearQuarterCompleteClicked(params))
                            BottomSheetType.GENRE -> viewModel.onAction(RankingAction.OnGenreCompleteClicked(params))
                            else -> {}
                        }
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
            APErrorScreen(
                onClick = { viewModel.onAction(RankingAction.OnRetryClicked) }
            )
        }
        UiState.Success -> {
            RankingScreen(
                bottomNav = bottomNav,
                state = state,
                onAction = { action ->
                    when (action) {
                        RankingAction.NavigateToSearch -> onNavigateToSearch()
                        is RankingAction.NavigateToInfoAnime -> onNavigateToInfoAnime(action.animeId)
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
            initSheetType = data.sheetType,
            onDismiss = data.onDismiss,
            onApply = { data.onConfirm(it) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RankingScreen(
    bottomNav: @Composable () -> Unit,
    state: RankingState,
    onAction: (RankingAction) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.isLoading) {
        if (state.isLoading) {
            listState.scrollToItem(0)
        }
    }

    LaunchedEffect(listState, state.animes.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 3
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !state.isMoreDataLoading && state.animes.isNotEmpty() && state.hasMoreData) {
                    onAction(RankingAction.OnLoadMore)
                }
            }
    }

    Scaffold(
        topBar = { APMainTopAppBar(onNavigateToSearch = { onAction(RankingAction.NavigateToSearch) }) },
        bottomBar = bottomNav,
        containerColor = AniPickSurface
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
                    .background(AniPickWhite)
                    .padding(dimensionResource(R.dimen.padding_large)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        text = stringResource(R.string.ranking_chip_real_time),
                        isSelected = state.type == RankingType.REAL_TIME,
                        onClick = {
                            onAction(RankingAction.OnFilterRealTimeClicked)
                        }
                    )
                    val yearSeason = when {
                        state.type != RankingType.YEAR_SEASON -> stringResource(R.string.ranking_chip_year_quarter)
                        state.year == null -> stringResource(R.string.ranking_default_year)
                        state.year == stringResource(R.string.ranking_default_year) || state.quarter.name == stringResource(R.string.ranking_default_quarter) -> state.year
                        else -> "${state.year}/${state.quarter.name}"
                    }
                    FilterChip(
                        text = yearSeason,
                        isSelected = state.type == RankingType.YEAR_SEASON,
                        onClick = {
                            onAction(RankingAction.OnFilterYearQuarterClicked)
                        }
                    )
                    FilterChip(
                        text = stringResource(R.string.ranking_chip_all_time),
                        isSelected = state.type == RankingType.ALL_TIME,
                        onClick = {
                            onAction(RankingAction.OnFilterAllTimeClicked)
                        }
                    )
                }
                GenreFilterChip(
                    state = state,
                    onAction = onAction
                )
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickSurface
            )
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .background(AniPickWhite),
                contentPadding = PaddingValues(
                    horizontal = dimensionResource(R.dimen.padding_large),
                    vertical = dimensionResource(R.dimen.padding_extra_large)
                ),
                userScrollEnabled = !state.isLoading,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
            ) {
                if (state.isLoading) {
                    items(10) {
                        SkeletonAnimeItem()
                    }
                } else {
                    items(state.animes) { anime ->
                        RankingItem(
                            anime = anime,
                            onClick = { onAction(RankingAction.NavigateToInfoAnime(it)) }
                        )
                    }
                    item {
                        if (state.isMoreDataLoading) {
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

@Composable
private fun GenreFilterChip(
    modifier: Modifier = Modifier,
    state: RankingState,
    onAction: (RankingAction) -> Unit
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .clickable { onAction(RankingAction.OnFilterGenreClicked) }
            .border(
                width = dimensionResource(R.dimen.border_width_default),
                color = if (state.genre.id != -1) AniPickSecondary else AniPickGray50,
                shape = CircleShape
            )
            .padding(horizontal = dimensionResource(R.dimen.padding_default), vertical = dimensionResource(R.dimen.padding_small)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = state.genre.name.ifBlank { stringResource(R.string.ranking_chip_genre) },
            style = AniPick16Regular.copy(
                color = if (state.genre.id != -1) AniPickSecondary else AniPickBlack
            )
        )
        Icon(
            imageVector = ChevronDownIcon,
            contentDescription = stringResource(R.string.chevron_down_icon),
            tint = if (state.genre.id != -1) AniPickSecondary else AniPickGray100,
            modifier = Modifier
                .size(dimensionResource(R.dimen.icon_size_small))
        )
    }
}