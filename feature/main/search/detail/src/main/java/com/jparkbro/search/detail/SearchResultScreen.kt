package com.jparkbro.search.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.model.common.UiState
import com.jparkbro.model.enum.SearchType
import com.jparkbro.search.detail.components.SkeletonScreen
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APActorCard
import com.jparkbro.ui.components.APAnimeCard
import com.jparkbro.ui.components.APEmptyContent
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APSearchTopAppBar
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray300
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ChevronRightIcon
import com.jparkbro.ui.util.rememberGridInfo
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun SearchResultRoot(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    onNavigateToActor: (Long) -> Unit,
    onNavigateToStudio: (Long) -> Unit,
    viewModel: SearchResultViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                onNavigateBack = onNavigateBack,
            )
        }

        UiState.Error -> {
            APErrorScreen(
                onClick = { viewModel.onAction(SearchResultAction.OnRetryClicked) }
            )
        }

        UiState.Success -> {
            SearchResultScreen(
                state = state,
                onAction = { action ->
                    when (action) {
                        SearchResultAction.NavigateBack -> onNavigateBack()
                        is SearchResultAction.NavigateToInfoAnime -> onNavigateToInfoAnime(action.animeId)
                        is SearchResultAction.NavigateToActor -> onNavigateToActor(action.actorId)
                        is SearchResultAction.NavigateToStudio -> onNavigateToStudio(action.studioId)
                    }
                    viewModel.onAction(action)
                },
                focusManager = focusManager
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResultScreen(
    state: SearchResultState,
    onAction: (SearchResultAction) -> Unit,
    focusManager: FocusManager
) {
    Scaffold(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() },
        topBar = {
            APSearchTopAppBar(
                onNavigateBack = { onAction(SearchResultAction.NavigateBack) },
                state = state.searchKeyword,
                onSearch = { onAction(SearchResultAction.OnSearchClicked) },
                onClear = { onAction(SearchResultAction.OnClearSearchKeyword) },
            )
        },
        containerColor = AniPickWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PrimaryScrollableTabRow(
                selectedTabIndex = state.searchType.ordinal,
                containerColor = AniPickWhite,
                edgePadding = dimensionResource(R.dimen.padding_large),
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            selectedTabIndex = when (state.searchType) {
                                SearchType.ANIME -> 0
                                SearchType.ACTOR -> 1
                                SearchType.STUDIO -> 2
                            },
                        ),
                        height = dimensionResource(R.dimen.border_width_thick),
                        width = 64.dp,
                        color = AniPickBlack
                    )
                },
                divider = {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = dimensionResource(R.dimen.border_width_thick),
                        color = AniPickGray100
                    )
                }
            ) {
                SearchType.entries.forEach { type ->
                    val isTabSelected = state.searchType == type
                    Tab(
                        selected = state.searchType == type,
                        onClick = { onAction(SearchResultAction.OnTabChanged(type)) },
                        text = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Text(
                                    text = when (type) {
                                        SearchType.ANIME -> stringResource(R.string.search_anime)
                                        SearchType.ACTOR -> stringResource(R.string.search_actor)
                                        SearchType.STUDIO -> stringResource(R.string.search_studio)
                                    },
                                    style = AniPick16Normal.copy(
                                        color = if (isTabSelected) AniPickBlack else AniPickGray400
                                    )
                                )
                                Text(
                                    text = stringResource(R.string.search_count,
                                        when (type) {
                                            SearchType.ANIME -> state.animeCount
                                            SearchType.ACTOR -> state.actorCount
                                            SearchType.STUDIO -> state.studioCount
                                        }
                                    ),
                                    style = AniPick14Normal.copy(color = AniPickGray100)
                                )
                            }
                        }
                    )
                }
            }
            when (state.searchType) {
                SearchType.ANIME -> AnimeTab(
                    state = state,
                    onAction = onAction
                )
                SearchType.ACTOR -> ActorTab(
                    state = state,
                    onAction = onAction
                )
                SearchType.STUDIO -> StudioTab(
                    state = state,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
private fun AnimeTab(
    state: SearchResultState,
    onAction: (SearchResultAction) -> Unit
) {
    val listState = rememberLazyGridState()

    LaunchedEffect(listState, state.animes.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 6
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !state.isMoreDataLoading && state.animes.isNotEmpty()) {
                    onAction(SearchResultAction.OnLoadMore)
                }
            }
    }

    BoxWithConstraints(
        modifier = Modifier
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

        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(gridInfo.columns),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default)),
            contentPadding = PaddingValues(
                start = dimensionResource(R.dimen.padding_large),
                end = dimensionResource(R.dimen.padding_large),
                top = dimensionResource(R.dimen.padding_large),
            )
        ) {
            item(span = { GridItemSpan(gridInfo.columns) }) {
                Text(
                    text = stringResource(R.string.search_total_count_data, state.animeCount),
                    style = AniPick14Normal.copy(color = AniPickGray400)
                )
            }
            if (state.animes.isNotEmpty()) {
                items(state.animes) { anime ->
                    var hasLogged by rememberSaveable(anime.animeId) { mutableStateOf(false) }

                    LaunchedEffect(anime.animeId) {
                        if (!hasLogged) {
                            onAction(SearchResultAction.OnSubmitAnimeLog(anime.impressionLog ?: ""))
                            hasLogged = true
                        }
                    }

                    APAnimeCard(
                        cardWidth = gridInfo.itemWidth,
                        imageUrl = anime.coverImageUrl,
                        title = anime.title,
                        isSmallTitle = true,
                        maxLine = 2,
                        onClick = {
                            onAction(SearchResultAction.OnSubmitAnimeLog(anime.clickLog ?: ""))
                            onAction(SearchResultAction.NavigateToInfoAnime(anime.animeId ?: -1))
                        }
                    )
                }

                item(span = { GridItemSpan(gridInfo.columns) }) {
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
            } else {
                item(span = { GridItemSpan(gridInfo.columns) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxHeight * 0.7f),
                        contentAlignment = Alignment.Center
                    ) {
                        APEmptyContent(
                            comment = stringResource(R.string.search_empty_list),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActorTab(
    state: SearchResultState,
    onAction: (SearchResultAction) -> Unit
) {
    val listState = rememberLazyGridState()

    LaunchedEffect(listState, state.actors.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 6
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !state.isMoreDataLoading && state.actors.isNotEmpty()) {
                    onAction(SearchResultAction.OnLoadMore)
                }
            }
    }

    BoxWithConstraints(
        modifier = Modifier
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

        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(gridInfo.columns),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default)),
            contentPadding = PaddingValues(
                start = dimensionResource(R.dimen.padding_large),
                end = dimensionResource(R.dimen.padding_large),
                top = dimensionResource(R.dimen.padding_large),
            )
        ) {
            item(span = { GridItemSpan(gridInfo.columns) }) {
                Text(
                    text = stringResource(R.string.search_total_count_actor, state.actorCount),
                    style = AniPick14Normal.copy(color = AniPickGray400)
                )
            }

            if (state.actors.isNotEmpty()) {
                items(state.actors) { actor ->
                    APActorCard(
                        cardWidth = 128.dp,
                        imageUrl = actor.imageUrl,
                        name = actor.name,
                        maxLine = 2,
                        onClick = { onAction(SearchResultAction.NavigateToActor(actor.id ?: 0)) }
                    )
                }

                item(span = { GridItemSpan(gridInfo.columns) }) {
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
            } else {
                item(span = { GridItemSpan(gridInfo.columns) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxHeight * 0.7f),
                        contentAlignment = Alignment.Center
                    ) {
                        APEmptyContent(
                            comment = stringResource(R.string.search_empty_list),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StudioTab(
    state: SearchResultState,
    onAction: (SearchResultAction) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, state.studios.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 6
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !state.isMoreDataLoading && state.studios.isNotEmpty()) {
                    onAction(SearchResultAction.OnLoadMore)
                }
            }
    }

    BoxWithConstraints(
        modifier = Modifier
            .background(AniPickWhite)
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(dimensionResource(R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
        ) {
            item {
                Text(
                    text = stringResource(R.string.search_total_count_data, state.studioCount),
                    style = AniPick14Normal.copy(color = AniPickGray400),
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.spacing_small))
                )
            }
            if (state.studios.isNotEmpty()) {
                items(state.studios) { studio ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAction(SearchResultAction.NavigateToStudio(studio.studioId)) }
                            .padding(vertical = dimensionResource(R.dimen.padding_extra_small)),
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        studio.name?.let {
                            Text(
                                text = it,
                                style = AniPick16Normal.copy(color = AniPickBlack)
                            )
                        }
                        Icon(
                            imageVector = ChevronRightIcon,
                            contentDescription = stringResource(R.string.chevron_right_icon),
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.icon_size_small)),
                            tint = AniPickGray300
                        )
                    }
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
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxHeight * 0.7f),
                        contentAlignment = Alignment.Center
                    ) {
                        APEmptyContent(
                            comment = stringResource(R.string.search_empty_list),
                        )
                    }
                }
            }
        }
    }
}