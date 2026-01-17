package com.jparkbro.mypage.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.model.common.FormType
import com.jparkbro.model.common.UiState
import com.jparkbro.model.enum.DialogType
import com.jparkbro.model.enum.HomeDetailType
import com.jparkbro.model.enum.UserContentType
import com.jparkbro.mypage.detail.components.SkeletonScreen
import com.jparkbro.mypage.detail.components.SortDropdownBtn
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APActorCard
import com.jparkbro.ui.components.APAlertDialog
import com.jparkbro.ui.components.APAnimeCard
import com.jparkbro.ui.components.APConfirmDialog
import com.jparkbro.ui.components.APEmptyContent
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APReportReasonDialog
import com.jparkbro.ui.components.APReviewCard
import com.jparkbro.ui.components.APTitleTopAppBar
import com.jparkbro.ui.components.APToggleSwitch
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Bold
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Bold
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickGray450
import com.jparkbro.ui.theme.AniPickGray500
import com.jparkbro.ui.theme.AniPickPoint
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSecondary
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.StarFillIcon
import com.jparkbro.ui.util.ObserveAsEvents
import com.jparkbro.ui.util.rememberGridInfo
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun UserContentRoot(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    onNavigateToActor: (Long) -> Unit,
    onNavigateToReviewForm: (Long, FormType) -> Unit,
    viewModel: UserContentViewModel = hiltViewModel()
) {
    var dialogData by rememberSaveable { mutableStateOf<DialogData?>(null) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {

        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }

        UiState.Error -> {
            APErrorScreen(
                onClick = { viewModel.onAction(UserContentAction.OnRetryClicked) }
            )
        }

        UiState.Success -> {
            UserContentScreen(
                state = state,
                onAction = { action ->
                    when (action) {
                        UserContentAction.NavigateBack -> onNavigateBack()
                        is UserContentAction.NavigateToInfoAnime -> onNavigateToInfoAnime(action.animeId)
                        is UserContentAction.NavigateToActor -> onNavigateToActor(action.actorId)
                        is UserContentAction.NavigateToEditReview -> onNavigateToReviewForm(action.animeId, action.formType)
                    }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserContentScreen(
    state: UserContentState,
    onAction: (UserContentAction) -> Unit
) {
    Scaffold(
        topBar = {
            APTitleTopAppBar(
                title = when (state.contentType) {
                    UserContentType.WATCHLIST -> stringResource(R.string.user_content_header_watch_list_anime)
                    UserContentType.WATCHING -> stringResource(R.string.user_content_header_watching_anime)
                    UserContentType.FINISHED -> stringResource(R.string.user_content_header_finished_anime)
                    UserContentType.LIKED_ANIME -> stringResource(R.string.user_content_header_liked_anime)
                    UserContentType.LIKED_PERSON -> stringResource(R.string.user_content_header_liked_actor)
                    UserContentType.RATING_REVIEW -> stringResource(R.string.user_content_header_rated_works)
                    else -> ""
                },
                onNavigateBack = { onAction(UserContentAction.NavigateBack) },
            )
        },
        containerColor = AniPickSurface
    ) { innerPadding ->
        when (state.contentType) {
            UserContentType.RATING_REVIEW -> {
                ReviewsContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    state = state,
                    onAction = onAction,
                )
            }

            else -> {
                AnimeAndActorContent(
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
    state: UserContentState,
    onAction: (UserContentAction) -> Unit,
) {
    var isReviewOnly by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(listState, state.reviews.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 3
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !state.isMoreDataLoading && state.reviews.isNotEmpty() && state.hasMoreData) {
                    onAction(UserContentAction.OnLoadMore)
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
                top = dimensionResource(R.dimen.padding_huge),
            ),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.total_count, state.count ?: 0),
                        style = AniPick14Normal.copy(color = AniPickGray400)
                    )
                    SortDropdownBtn(
                        state = state,
                        onAction = onAction
                    )
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.user_content_only_review),
                        style = AniPick16Bold.copy(
                            color = if (isReviewOnly) AniPickSecondary else AniPickGray450
                        )
                    )
                    APToggleSwitch(
                        checked = isReviewOnly,
                        checkedColor = AniPickSecondary,
                        unCheckedColor = AniPickGray450,
                        onCheckedChange = { isReviewOnly = !isReviewOnly },
                    )
                }
            }
            val filteredReviews = state.reviews.filter { (it.content != null || !isReviewOnly) && it.reviewId != null }
            if (filteredReviews.isNotEmpty()) {
                items(
                    items = filteredReviews,
                    key = { it.reviewId!! }
                ) { review ->
                    APReviewCard(
                        review = review,
                        onClickEdit = { animeId, reviewId ->
                            onAction(UserContentAction.NavigateToEditReview(animeId, FormType.EDIT))
                        },
                        onClickDelete = { reviewId ->
                            onAction(UserContentAction.OnReviewDeleteClicked(reviewId))
                        },
                        onClickLiked = { reviewId, animeId, isLiked , result ->
                            onAction(UserContentAction.OnReviewLikeClicked(reviewId, animeId, isLiked, result))
                        },
                        onNavigateInfoAnime= { animeId ->
                            onAction(UserContentAction.NavigateToInfoAnime(animeId))
                        }
                    )
                }
            } else {
               item {
                   APEmptyContent(
                       modifier = Modifier
                           .fillMaxWidth(),
                       comment = stringResource(R.string.user_content_empty_review)
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
        }
    }
}

@Composable
private fun AnimeAndActorContent(
    modifier: Modifier = Modifier,
    state: UserContentState,
    onAction: (UserContentAction) -> Unit,
) {
    val listState = rememberLazyGridState()

    LaunchedEffect(listState, state.animes.size, state.actors.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 6
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !state.isMoreDataLoading && (state.animes.isNotEmpty() || state.actors.isNotEmpty())) {
                    onAction(UserContentAction.OnLoadMore)
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
            horizontalPadding = horizontalPadding * 2,
            spacing = spacing,
            defaultItemWidth = 128.dp,
            minColumns = 3,
            maxColumns = 5
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AniPickWhite)
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickGray100
            )
            if (state.animes.isEmpty() && state.actors.isEmpty()) {
                APEmptyContent(
                    modifier = Modifier
                        .fillMaxSize(),
                    comment = stringResource(
                        when (state.contentType) {
                            UserContentType.WATCHING -> R.string.user_content_empty_watching
                            UserContentType.FINISHED -> R.string.user_content_empty_finished
                            else -> R.string.user_content_empty_watch_list
                        }
                    )
                )
            } else {
                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Fixed(gridInfo.columns),
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalArrangement = Arrangement.spacedBy(
                        dimensionResource(R.dimen.spacing_medium)
                    ),
                    contentPadding = PaddingValues(
                        start = dimensionResource(R.dimen.padding_large),
                        end = dimensionResource(R.dimen.padding_large),
                        top = dimensionResource(R.dimen.padding_huge),
                    )
                ) {
                    item(span = { GridItemSpan(gridInfo.columns) }) {
                        Text(
                            text = stringResource(
                                when (state.contentType) {
                                    UserContentType.LIKED_PERSON -> R.string.total_count_actor
                                    else -> R.string.total_count
                                }, state.count ?: 0),
                            style = AniPick14Normal.copy(color = AniPickGray400)
                        )
                    }
                    when (state.contentType) {
                        UserContentType.LIKED_PERSON -> {
                            items(state.actors) { actor ->
                                APActorCard(
                                    cardWidth = 128.dp,
                                    imageUrl = actor.imageUrl,
                                    name = actor.name,
                                    maxLine = 2,
                                    onClick = { onAction(UserContentAction.NavigateToActor(actor.id ?: 0)) }
                                )
                            }
                        }
                        else -> {
                            items(state.animes) { anime ->
                                APAnimeCard(
                                    cardWidth = gridInfo.itemWidth,
                                    imageUrl = anime.coverImageUrl,
                                    title = anime.title,
                                    isSmallTitle = true,
                                    maxLine = 2,
                                    description = {
                                        if (state.contentType == UserContentType.FINISHED) {
                                            Box(
                                                modifier = Modifier.fillMaxWidth(),
                                                contentAlignment = Alignment.CenterStart
                                            ) {
                                                if (anime.myRating == null) {
                                                    Text(
                                                        text = stringResource(R.string.user_content_rating_null),
                                                        style = AniPick12Normal.copy(color = AniPickGray400),
                                                    )
                                                } else {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = stringResource(R.string.user_content_my_rating),
                                                            style = AniPick12Normal.copy(color = AniPickGray400),
                                                        )
                                                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                                                        Image(
                                                            imageVector = StarFillIcon,
                                                            contentDescription = stringResource(R.string.fill_star_icon),
                                                            modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small)),
                                                        )
                                                        Spacer(modifier = Modifier.width(2.dp))
                                                        Text(
                                                            text = anime.myRating.toString(),
                                                            style = AniPick14Bold.copy(color = AniPickPoint),
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    onClick = { onAction(UserContentAction.NavigateToInfoAnime(anime.animeId ?: -1)) }
                                )
                            }
                        }
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
}