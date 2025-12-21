package com.jparkbro.home.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.home.detail.components.DropdownButton
import com.jparkbro.home.detail.components.RecommendationBanner
import com.jparkbro.home.detail.components.SkeletonScreen
import com.jparkbro.model.common.FormType
import com.jparkbro.model.common.UiState
import com.jparkbro.model.enum.DialogType
import com.jparkbro.model.enum.HomeDetailType
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APAlertDialog
import com.jparkbro.ui.components.APAnimeCard
import com.jparkbro.ui.components.APConfirmDialog
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APReportReasonDialog
import com.jparkbro.ui.components.APReviewCard
import com.jparkbro.ui.components.APSnackBarRe
import com.jparkbro.ui.components.APTitleTopAppBar
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.util.ObserveAsEvents
import com.jparkbro.ui.util.rememberGridInfo
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun HomeDetailRoot(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateToReviewForm: (Int, Int, FormType) -> Unit,
    viewModel: HomeDetailViewModel = hiltViewModel()
) {
    var dialogData by rememberSaveable { mutableStateOf<DialogData?>(null) }
    var snackBarData by rememberSaveable { mutableStateOf<List<SnackBarData>>(emptyList()) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HomeDetailEvent.ShowDialog -> {
                dialogData = event.dialogData.copy(
                    onDismiss = { dialogData = null },
                    onConfirm = {
                        event.dialogData.onConfirm(it)
                        dialogData = null
                    }
                )
            }
            is HomeDetailEvent.ShowSnackBar -> {
                snackBarData = snackBarData + event.snackBarData.copy(
                    onDismiss = { snackBarData = snackBarData.drop(1) }
                )
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(state)
        }

        UiState.Error -> {
            APErrorScreen(
                onClick = { viewModel.onAction(HomeDetailAction.OnRetryClicked) }
            )
        }

        UiState.Success -> {
            HomeDetailScreen(
                state = state,
                onAction = { action ->
                    when (action) {
                        HomeDetailAction.NavigateBack -> onNavigateBack()
                        is HomeDetailAction.NavigateToAnimeDetail -> onNavigateToAnimeDetail(action.animeId)
                        is HomeDetailAction.OnReviewEditClicked -> onNavigateToReviewForm(action.animeId, action.reviewId, action.type)
                    }
                    viewModel.onAction(action)
                }
            )
        }
    }

    dialogData?.let { dialogData ->
        when (dialogData.type) {
            DialogType.CONFIRM -> APConfirmDialog(dialogData)
            DialogType.SELECT -> APReportReasonDialog(dialogData)
            DialogType.ALERT -> APAlertDialog(dialogData)
        }
    }

    snackBarData.firstOrNull()?.let { snackBarData ->
        APSnackBarRe(snackBarData)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeDetailScreen(
    state: HomeDetailState,
    onAction: (HomeDetailAction) -> Unit
) {
    Scaffold(
        topBar = {
            APTitleTopAppBar(
                title = state.type.title,
                onNavigateBack = { onAction(HomeDetailAction.NavigateBack) },
            )
        },
        containerColor = AniPickSurface
    ) { innerPadding ->
        when (state.type) {
            HomeDetailType.RECENT_REVIEWS -> {
                ReviewsContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    state = state,
                    onAction = onAction,
                )
            }

            else -> {
                AnimesContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    state = state,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun ReviewsContent(
    modifier: Modifier = Modifier,
    state: HomeDetailState,
    onAction: (HomeDetailAction) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, state.animes.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 3
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !state.isMoreDataLoading && state.animes.isNotEmpty()) {
                    onAction(HomeDetailAction.OnLoadMore)
                }
            }
    }

    Column {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = dimensionResource(R.dimen.border_width_default),
            color = AniPickGray100
        )
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(
                start = dimensionResource(R.dimen.padding_large),
                end = dimensionResource(R.dimen.padding_large),
                top = dimensionResource(R.dimen.padding_medium),
            ),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            items(state.reviews) { review ->
                APReviewCard(
                    review = review,
                    onClickEdit = { animeId, reviewId ->
                        onAction(HomeDetailAction.OnReviewEditClicked(animeId, reviewId, FormType.EDIT))
                    },
                    onClickDelete = { reviewId ->
                        onAction(HomeDetailAction.OnReviewDeleteClicked(reviewId))
                    },
                    onCLickReport = { reviewId ->
                        onAction(HomeDetailAction.OnReviewReportClicked(reviewId))
                    },
                    onClickBlock = { userId ->
                        onAction(HomeDetailAction.OnUserBlockClicked(userId))
                    },
                    onClickLiked = { reviewId, isLiked , result ->
                        onAction(HomeDetailAction.OnReviewLikeClicked(reviewId, isLiked, result))
                    },
                    onNavigateAnimeDetail = { animeId ->
                        onAction(HomeDetailAction.NavigateToAnimeDetail(animeId))
                    }
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

@Composable
private fun AnimesContent(
    modifier: Modifier = Modifier,
    state: HomeDetailState,
    onAction: (HomeDetailAction) -> Unit
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
                    onAction(HomeDetailAction.OnLoadMore)
                }
            }
    }

    BoxWithConstraints(
        modifier = modifier
            .background(AniPickWhite)
    ) {
        val horizontalPadding = dimensionResource(R.dimen.padding_large)
        val spacing = 8.dp

        val gridInfo = rememberGridInfo(
            availableWidth = maxWidth,
            horizontalPadding = horizontalPadding,
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
                color = AniPickGray100
            )
            LazyVerticalGrid(
                state = listState,
                columns = GridCells.Fixed(gridInfo.columns),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.spacing_extra_large)
                ),
                contentPadding = PaddingValues(
                    horizontal = horizontalPadding,
                    vertical = dimensionResource(R.dimen.spacing_extra_large)
                )
            ) {
                item(span = { GridItemSpan(gridInfo.columns) }) {
                    when (state.type) {
                        HomeDetailType.RECOMMENDS, HomeDetailType.SIMILAR_TO_WATCHED -> {
                            RecommendationBanner(state)
                        }
                        HomeDetailType.UPCOMING_RELEASE -> {
                            DropdownButton(
                                state = state,
                                onAction = onAction
                            )
                        }
                        else -> Unit
                    }

                }

                items(state.animes) { anime ->
                    APAnimeCard(
                        cardWidth = gridInfo.itemWidth,
                        imageUrl = anime.coverImageUrl,
                        title = anime.title,
                        isSmallTitle = true,
                        maxLine = 2,
                        description = {
                            anime.releaseDate?.let {
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
                        onClick = { onAction(HomeDetailAction.NavigateToAnimeDetail(anime.animeId ?: -1)) }
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
            }
        }
    }
}

@DevicePreviews
@Composable
private fun HomeDetailScreenPreview() {
    HomeDetailScreen(
        state = HomeDetailState(
            referenceAnimeTitle = "",
        ),
        onAction = {}
    )
}