package com.jparkbro.actor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.jparkbro.actor.component.SkeletonScreen
import com.jparkbro.model.common.UiState
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APAnimationLikeIcon
import com.jparkbro.ui.components.APAnimeCard
import com.jparkbro.ui.components.APCastCard
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APTitleTopAppBar
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.util.rememberGridInfo
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun ActorRoot(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    viewModel: ActorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                onAction = { action ->
                    when (action) {
                        ActorAction.NavigateBack -> onNavigateBack()
                    }
                }
            )
        }

        UiState.Error -> {
            APErrorScreen(
                onClick = { viewModel.onAction(ActorAction.OnRetryClicked) }
            )
        }

        UiState.Success -> {
            ActorScreen(
                state = state,
                onAction = { action ->
                    when (action) {
                        ActorAction.NavigateBack -> onNavigateBack()
                        is ActorAction.NavigateToInfoAnime -> onNavigateToInfoAnime(action.animeId)
                    }
                    viewModel.onAction(action)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActorScreen(
    state: ActorState,
    onAction: (ActorAction) -> Unit,
) {
    val listState = rememberLazyGridState()

    LaunchedEffect(listState, state.result.works.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 6
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !state.isLoading && state.result.works.isNotEmpty() && state.hasMoreData) {
                    onAction(ActorAction.LoadMoreAnime)
                }
            }
    }

    Scaffold(
        topBar = {
            APTitleTopAppBar(
                title = stringResource(R.string.actor_header),
                onNavigateBack = { onAction(ActorAction.NavigateBack) },
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = dimensionResource(R.dimen.border_width_default),
                    color = AniPickSurface
                )
                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Fixed(gridInfo.columns),
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                    contentPadding = PaddingValues(dimensionResource(R.dimen.spacing_large))
                ) {
                    item(span = { GridItemSpan(gridInfo.columns) }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = state.result.profileImageUrl?.takeIf { !it.contains("default.jpg") },
                                    contentDescription = stringResource(R.string.character_img),
                                    error = painterResource(R.drawable.thumbnail_img),
                                    placeholder = painterResource(R.drawable.thumbnail_img),
                                    modifier = Modifier
                                        .width(100.dp)
                                        .clip(AniPickSmallShape)
                                        .aspectRatio(3f / 4f),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_large)))
                                state.result.name?.let {
                                    Text(
                                        text = it,
                                        style = AniPick20Bold.copy(color = AniPickBlack)
                                    )
                                }
                                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                                APAnimationLikeIcon(
                                    isLiked = state.result.isLiked ?: false,
                                    isLikingAnime = state.isLikedLoading,
                                    onClick = { onAction(ActorAction.OnActorLikeClicked(state.result.isLiked ?: false)) }
                                )
                            }
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = dimensionResource(R.dimen.border_width_large),
                                color = AniPickSurface
                            )
                            Text(
                                text = stringResource(R.string.actor_works_title),
                                style = AniPick16Normal.copy(color = AniPickBlack),
                            )
                            Text(
                                text = stringResource(R.string.total_count, state.result.count ?: 0),
                                style = AniPick14Normal.copy(color = AniPickGray400),
                            )
                        }
                    }

                    items(state.works) { work ->
                        APCastCard(
                            cardWidth = gridInfo.itemWidth,
                            work = work,
                            onClick = { onAction(ActorAction.NavigateToInfoAnime(it) )}
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