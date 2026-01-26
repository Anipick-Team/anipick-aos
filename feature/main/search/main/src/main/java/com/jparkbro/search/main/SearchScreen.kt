package com.jparkbro.search.main

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.model.common.UiState
import com.jparkbro.search.main.components.SkeletonScreen
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APAnimeCard
import com.jparkbro.ui.components.APBackStackTopAppBar
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APSearchTopAppBar
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.CloseIcon
import com.jparkbro.ui.util.ObserveAsEvents
import com.jparkbro.ui.util.rememberGridInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchRoot(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    onNavigateToSearchResult: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SearchEvent.SearchSuccess -> {
                onNavigateToSearchResult(event.keyword)
            }
        }
    }

    val focusManager = LocalFocusManager.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                onNavigateBack = onNavigateBack,
            )
        }

        UiState.Error -> {
            Scaffold(
                topBar = {
                    APBackStackTopAppBar(
                        onNavigateBack = onNavigateBack,
                    )
                },
                containerColor = AniPickWhite
            ) {
                APErrorScreen(
                    onClick = { viewModel.onAction(SearchAction.OnRetryClicked) },
                    modifier = Modifier.padding(it)
                )
            }
        }

        UiState.Success -> {
            SearchScreen(
                state = state,
                onAction = { action ->
                    when (action) {
                        SearchAction.NavigateBack -> onNavigateBack()
                        is SearchAction.NavigateToInfoAnime -> onNavigateToInfoAnime(action.animeId)
                        is SearchAction.NavigateToSearchResult -> onNavigateToSearchResult(action.keyword)
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
private fun SearchScreen(
    state: SearchState,
    onAction: (SearchAction) -> Unit,
    focusManager: FocusManager,
) {
    Scaffold(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() },
        topBar = {
            APSearchTopAppBar(
                onNavigateBack = { onAction(SearchAction.NavigateBack) },
                state = state.searchKeyword,
                onSearch = { onAction(SearchAction.OnSearchClicked) },
                onClear = { onAction(SearchAction.OnClearSearchKeyword) },
            )
        },
        containerColor = AniPickWhite
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
            ) {
                stickyHeader {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = dimensionResource(R.dimen.border_width_default),
                        color = AniPickSurface
                    )
                }
                if (state.recentKeywords.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = dimensionResource(R.dimen.padding_large)),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.search_recent_keyword),
                                    style = AniPick20Bold.copy(color = AniPickBlack)
                                )
                                Text(
                                    text = stringResource(R.string.search_delete_all),
                                    style = AniPick14Normal.copy(color = AniPickGray100),
                                    modifier = Modifier
                                        .clickable { onAction(SearchAction.OnDeleteAllSearchKeywords) }
                                )
                            }
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                                contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large))
                            ) {
                                items(state.recentKeywords) { keyword ->
                                    Row(
                                        modifier = Modifier
                                            .border(dimensionResource(R.dimen.border_width_default), AniPickGray50, CircleShape)
                                            .clip(CircleShape)
                                            .clickable { onAction(SearchAction.NavigateToSearchResult(keyword)) }
                                            .padding(horizontal = dimensionResource(R.dimen.padding_default), vertical = dimensionResource(R.dimen.padding_small))
                                        ,
                                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = keyword,
                                            style = AniPick14Normal.copy(color = AniPickBlack)
                                        )
                                        Icon(
                                            imageVector = CloseIcon,
                                            contentDescription = stringResource(R.string.close_icon),
                                            tint = AniPickGray100,
                                            modifier = Modifier
                                                .size(dimensionResource(R.dimen.icon_size_small))
                                                .clip(CircleShape)
                                                .clickable { onAction(SearchAction.OnDeleteSearchKeyword(keyword)) }
                                        )
                                    }
                                }
                            }
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = dimensionResource(R.dimen.border_width_extra_large),
                                color = AniPickSurface
                            )
                        }
                    }
                }
                item {
                    Text(
                        text = stringResource(R.string.search_popular_anime),
                        style = AniPick20Bold.copy(color = AniPickBlack),
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_large))
                    )
                }
                item {
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
                                onClick = { onAction(SearchAction.NavigateToInfoAnime(anime.animeId ?: -1)) }
                            )
                        }
                    }
                }
            }
        }
    }
}