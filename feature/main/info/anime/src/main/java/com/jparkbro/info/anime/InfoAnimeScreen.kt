package com.jparkbro.info.anime

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.jparkbro.info.anime.components.AdditionalInfoSection
import com.jparkbro.info.anime.components.AnimeInfoRow
import com.jparkbro.info.anime.components.SkeletonScreen
import com.jparkbro.info.anime.components.WatchStatusSelector
import com.jparkbro.model.common.FormType
import com.jparkbro.model.common.UiState
import com.jparkbro.model.enum.AnimeInfoTab
import com.jparkbro.model.enum.DialogType
import com.jparkbro.model.enum.MyDropdownMenu
import com.jparkbro.model.enum.ReviewSortType
import com.jparkbro.model.enum.WatchStatus
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APAlertDialog
import com.jparkbro.ui.components.APAnimationLikeIcon
import com.jparkbro.ui.components.APAnimeCard
import com.jparkbro.ui.components.APCastPairCard
import com.jparkbro.ui.components.APConfirmDialog
import com.jparkbro.ui.components.APEmptyContent
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APReportReasonDialog
import com.jparkbro.ui.components.APReviewCard
import com.jparkbro.ui.components.APSnackBarRe
import com.jparkbro.ui.components.APToggleSwitch
import com.jparkbro.ui.components.updateRatingFromPosition
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Bold
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Bold
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick18ExtraBold
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPick24Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickPoint
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSecondary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.BorderChevronLeftIcon
import com.jparkbro.ui.theme.ChevronDownIcon
import com.jparkbro.ui.theme.ChevronLeftIcon
import com.jparkbro.ui.theme.ChevronUpIcon
import com.jparkbro.ui.theme.FavoriteOffIcon
import com.jparkbro.ui.theme.MoreVerticalIcon
import com.jparkbro.ui.theme.ShareIcon
import com.jparkbro.ui.theme.StarFillIcon
import com.jparkbro.ui.theme.StarOutlineIcon
import com.jparkbro.ui.util.ObserveAsEvents
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun InfoAnimeRoot(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    onNavigateToInfoSeries: (Long, String) -> Unit,
    onNavigateToInfoRecommend: (Long) -> Unit,
    onNavigateToReviewForm: (Long, FormType) -> Unit,
    onNavigateToInfoCharacter: (Long) -> Unit,
    onNavigateToActor: (Long) -> Unit,
    onNavigateToStudio: (Long) -> Unit,
    viewModel: InfoAnimeViewModel = hiltViewModel()
) {
    var dialogData by rememberSaveable { mutableStateOf<DialogData?>(null) }
    var snackBarData by rememberSaveable { mutableStateOf<List<SnackBarData>>(emptyList()) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is InfoAnimeEvent.ShowDialog -> {
                dialogData = event.dialogData.copy(
                    onDismiss = { dialogData = null },
                    onConfirm = {
                        event.dialogData.onConfirm(it)
                        dialogData = null
                    }
                )
            }
            is InfoAnimeEvent.ShowSnackBar -> {
                snackBarData = snackBarData + event.snackBarData.copy(
                    onDismiss = { snackBarData = snackBarData.drop(1) }
                )
            }
        }
    }

    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableStateOf(AnimeInfoTab.INFO) }

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                onAction = { action ->
                    when (action) {
                        InfoAnimeAction.NavigateBack -> onNavigateBack()
                    }
                }
            )
        }

        UiState.Error -> {
            APErrorScreen(
                onClick = { viewModel.onAction(InfoAnimeAction.OnRetryClicked) }
            )
        }

        UiState.Success -> {
            InfoAnimeScreen(
                state = state,
                onAction = { action ->
                    when (action) {
                        InfoAnimeAction.NavigateBack -> onNavigateBack()
                        is InfoAnimeAction.OnTabClicked -> selectedTab = action.tab
                        is InfoAnimeAction.OnShareClicked -> {
                            val sendIntent = Intent().apply {
                                this.action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, action.shareLink)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        }
                        is InfoAnimeAction.NavigateToStudio -> onNavigateToStudio(action.studioId)
                        is InfoAnimeAction.NavigateToCasts -> onNavigateToInfoCharacter(action.animeId)
                        is InfoAnimeAction.NavigateToSeries -> onNavigateToInfoSeries(action.animeId, action.title)
                        is InfoAnimeAction.NavigateToRecommend -> onNavigateToInfoRecommend(action.animeId)
                        is InfoAnimeAction.NavigateToActor -> onNavigateToActor(action.actorId)
                        is InfoAnimeAction.NavigateToEditReview -> onNavigateToReviewForm(action.animeId, action.type)
                        is InfoAnimeAction.NavigateToAnimeDetail -> onNavigateToInfoAnime(action.animeId)
                    }
                    viewModel.onAction(action)
                },
                selectedTab = selectedTab,
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

@Composable
private fun InfoAnimeScreen(
    state: InfoAnimeState,
    onAction: (InfoAnimeAction) -> Unit,
    selectedTab: AnimeInfoTab,
) {
    val lazyListState = rememberLazyListState()

    val isTopComponentVisible by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0
        }
    }
    val showStickyHeader by remember {
        derivedStateOf {
            !isTopComponentVisible
        }
    }

    LaunchedEffect(lazyListState, state.reviews.size) {
        snapshotFlow { lazyListState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 2
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !state.isLoadingMoreReviews && state.reviews.isNotEmpty()) {
                    onAction(InfoAnimeAction.LoadMoreReviews)
                }
            }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .background(AniPickWhite)
            .windowInsetsPadding(
                WindowInsets(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
            )
    ) {
        item {
            ExpandedHeader(
                state = state,
                onAction = onAction,
            )
        }
        stickyHeader {
            CollapseHeader(
                selectedTab = selectedTab,
                showStickyHeader = showStickyHeader,
                state = state,
                onAction = onAction,
            )
        }
        item {
            when (selectedTab) {
                AnimeInfoTab.INFO -> InfoTab(
                    state = state,
                    onAction = onAction,
                )

                AnimeInfoTab.REVIEW -> ReviewTab(
                    state = state,
                    onAction = onAction,
                )
            }
        }
    }

}

@Composable
private fun ExpandedHeader(
    state: InfoAnimeState,
    onAction: (InfoAnimeAction) -> Unit,
) {
    val shareLink = stringResource(R.string.anime_detail_shared_link, state.animeInfo?.animeId ?: 0)

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = state.animeInfo?.bannerImageUrl,
                contentDescription = stringResource(R.string.anime_banner_img),
                error = painterResource(R.drawable.default_banner_img),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(AniPickGray50),
                contentScale = ContentScale.FillHeight
            )
            Icon(
                imageVector = BorderChevronLeftIcon,
                contentDescription = stringResource(R.string.back_stack_icon),
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(
                        top = dimensionResource(R.dimen.padding_extra_huge),
                        start = dimensionResource(R.dimen.padding_large)
                    )
                    .size(dimensionResource(R.dimen.icon_size_large))
                    .clip(CircleShape)
                    .clickable { onAction(InfoAnimeAction.NavigateBack) }
            )
            AsyncImage(
                model = state.animeInfo?.coverImageUrl?.takeIf { !it.contains("default.jpg") },
                contentDescription = stringResource(R.string.anime_cover_img),
                error = painterResource(R.drawable.thumbnail_img),
                placeholder = painterResource(R.drawable.thumbnail_img),
                modifier = Modifier
                    .padding(
                        bottom = dimensionResource(R.dimen.padding_large),
                        end = dimensionResource(R.dimen.padding_large)
                    )
                    .width(124.dp)
                    .clip(AniPickSmallShape)
                    .aspectRatio(2f / 3f)
                    .align(Alignment.BottomEnd)
                    .border(
                        width = dimensionResource(R.dimen.border_width_thick),
                        color = AniPickWhite,
                        shape = AniPickSmallShape
                    ),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    state.animeInfo?.title?.let {
                        Text(
                            text = it,
                            style = AniPick20Bold.copy(color = AniPickBlack),
                        )
                    }
                    APAnimationLikeIcon(
                        isLiked = state.animeInfo?.isLiked ?: false,
                        isLikingAnime = state.isLikingAnime,
                        onClick = { onAction(InfoAnimeAction.OnAnimeLikeClicked(it)) }
                    )
                }
                Icon(
                    imageVector = ShareIcon,
                    contentDescription = stringResource(R.string.share_icon),
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_extra_large))
                        .clip(AniPickSmallShape)
                        .clickable { onAction(InfoAnimeAction.OnShareClicked(shareLink)) }
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = StarFillIcon,
                    contentDescription = stringResource(R.string.fill_star_icon),
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_medium))
                )
                Text(
                    text = if (state.animeInfo?.averageRating == null) "0.0" else state.animeInfo.averageRating.toString(),
                    style = AniPick14Bold.copy(color = AniPickPoint),
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_large)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WatchStatusSelector(
                text = stringResource(R.string.watch_list_anime),
                status = state.animeInfo?.watchStatus == WatchStatus.WATCHLIST,
                enabled = !state.isWatchStatusChanging,
                onClick = { onAction(InfoAnimeAction.OnWatchStatusClicked(WatchStatus.WATCHLIST)) }
            )
            WatchStatusSelector(
                text = stringResource(R.string.watching_anime),
                status = state.animeInfo?.watchStatus == WatchStatus.WATCHING,
                enabled = !state.isWatchStatusChanging,
                onClick = { onAction(InfoAnimeAction.OnWatchStatusClicked(WatchStatus.WATCHING)) }
            )
            WatchStatusSelector(
                text = stringResource(R.string.finished_anime),
                status = state.animeInfo?.watchStatus == WatchStatus.FINISHED,
                enabled = !state.isWatchStatusChanging,
                onClick = { onAction(InfoAnimeAction.OnWatchStatusClicked(WatchStatus.FINISHED)) }
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.spacing_extra_large)),
            thickness = dimensionResource(R.dimen.border_width_large),
            color = AniPickSurface
        )
    }
}

@Composable
private fun CollapseHeader(
    state: InfoAnimeState,
    onAction: (InfoAnimeAction) -> Unit,
    selectedTab: AnimeInfoTab,
    showStickyHeader: Boolean,
) {
    if (showStickyHeader) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AniPickWhite)
                .padding(dimensionResource(R.dimen.padding_default))
        ) {
            Icon(
                imageVector = ChevronLeftIcon,
                contentDescription = stringResource(R.string.back_stack_icon),
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_large))
                    .align(Alignment.CenterStart)
                    .clip(CircleShape)
                    .clickable { onAction(InfoAnimeAction.NavigateBack) }
            )
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = dimensionResource(R.dimen.padding_huge)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                state.animeInfo?.title?.let {
                    Text(
                        text = it,
                        style = AniPick20Bold.copy(color = AniPickBlack),
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
                APAnimationLikeIcon(
                    isLiked = state.animeInfo?.isLiked ?: false,
                    isLikingAnime = state.isLikingAnime,
                    onClick = { onAction(InfoAnimeAction.OnAnimeLikeClicked(it)) }
                )
            }
        }
    }
    SecondaryTabRow(
        selectedTabIndex = when (selectedTab) {
            AnimeInfoTab.INFO -> 0
            AnimeInfoTab.REVIEW -> 1
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(AniPickWhite)
            .padding(horizontal = dimensionResource(R.dimen.padding_large)),
        containerColor = AniPickWhite,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(
                    selectedTabIndex = when (selectedTab) {
                        AnimeInfoTab.INFO -> 0
                        AnimeInfoTab.REVIEW -> 1
                    },
                ),
                height = dimensionResource(R.dimen.border_width_thick),
                color = AniPickGray50
            )
        },
        divider = {}
    ) {
        AnimeInfoTab.entries.forEachIndexed { index, tab ->
            val isSelected = selectedTab == tab
            Tab(
                selected = isSelected,
                onClick = { onAction(InfoAnimeAction.OnTabClicked(tab)) },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = when (tab) {
                        AnimeInfoTab.INFO -> stringResource(R.string.anime_detail_tab_info)
                        AnimeInfoTab.REVIEW -> stringResource(R.string.anime_detail_tab_review, state.animeInfo?.reviewCount ?: 0)
                    },
                    style = AniPick14Normal.copy(
                        color = if (isSelected) AniPickBlack else AniPickGray100
                    ),
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}

@Composable
private fun InfoTab(
    state: InfoAnimeState,
    onAction: (InfoAnimeAction) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isTextOverflowing by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.padding_medium)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
        ) {
            state.animeInfo?.description?.let {
                Text(
                    text = it,
                    style = AniPick14Normal.copy(color = AniPickBlack),
                    maxLines = if (expanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult ->
                        isTextOverflowing = textLayoutResult.hasVisualOverflow
                    }
                )
            }
            if (isTextOverflowing || expanded) {
                Row(
                    modifier = Modifier
                        .clickable { expanded = !expanded },
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(if (expanded) R.string.collapse else R.string.expand),
                        style = AniPick14Normal.copy(color = AniPickPrimary)
                    )
                    Icon(
                        imageVector = if (expanded) ChevronUpIcon else ChevronDownIcon,
                        contentDescription = stringResource(R.string.chevron_up_down_icon),
                        tint = AniPickPrimary,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_small))
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_extra_large)),
            thickness = dimensionResource(R.dimen.border_width_large),
            color = AniPickSurface
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_large))
                .padding(top = dimensionResource(R.dimen.padding_medium), bottom = dimensionResource(R.dimen.space_64)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
        ) {
            AnimeInfoRow(
                label = stringResource(R.string.anime_detail_label_type),
                content = {
                    state.animeInfo?.type?.let {
                        Text(
                            text = it,
                            style = AniPick14Normal.copy(color = AniPickGray400)
                        )
                    }
                }
            )
            AnimeInfoRow(
                label = stringResource(R.string.anime_detail_label_genre),
                content = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.animeInfo?.genres?.forEach { genre ->
                            Text(
                                text = genre.name,
                                style = AniPick12Normal.copy(color = AniPickPrimary),
                                modifier = Modifier
                                    .background(AniPickPrimary.copy(alpha = 0.1f), AniPickSmallShape)
                                    .padding(horizontal = dimensionResource(R.dimen.padding_small), vertical = dimensionResource(R.dimen.padding_extra_small)),
                            )
                        }
                    }
                }
            )
            AnimeInfoRow(
                label = stringResource(R.string.anime_detail_label_date),
                content = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.animeInfo?.status?.let {
                            Text(
                                text = it,
                                style = AniPick14Normal.copy(color = AniPickBlack),
                                modifier = Modifier
                                    .background(AniPickGray50, CircleShape)
                                    .padding(horizontal = dimensionResource(R.dimen.padding_medium), vertical = dimensionResource(R.dimen.padding_extra_small)),
                                textAlign = TextAlign.Center
                            )
                        }
                        state.animeInfo?.airDate?.let {
                            Text(
                                text = it,
                                style = AniPick14Normal.copy(color = AniPickGray400)
                            )
                        }
                    }
                }
            )
            AnimeInfoRow(
                label = stringResource(R.string.anime_detail_label_episode),
                content = {
                    state.animeInfo?.episode?.let {
                        Text(
                            text = stringResource(R.string.anime_detail_episode_count, it),
                            style = AniPick14Normal.copy(color = AniPickGray400)
                        )
                    }
                }
            )
            AnimeInfoRow(
                label = stringResource(R.string.anime_detail_label_age),
                content = {
                    state.animeInfo?.age?.let {
                        Text(
                            text = it,
                            style = AniPick14Normal.copy(color = AniPickGray400)
                        )
                    }
                }
            )
            AnimeInfoRow(
                label = stringResource(R.string.anime_detail_label_studio),
                content = {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small), Alignment.End)
                    ) {
                        state.animeInfo?.studios?.forEach { studio ->
                            studio.name?.let {
                                Text(
                                    text = it,
                                    style = AniPick14Normal.copy(color = AniPickSecondary),
                                    modifier = Modifier
                                        .clickable { onAction(InfoAnimeAction.NavigateToStudio(studio.studioId)) }
                                        .drawBehind {
                                            val strokeWidth = 1.dp.toPx()
                                            val y = size.height - strokeWidth / 2
                                            drawLine(
                                                color = AniPickSecondary,
                                                start = Offset(0f, y),
                                                end = Offset(size.width, y),
                                                strokeWidth = strokeWidth
                                            )
                                        }
                                )
                            }
                        }
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large))
        ) {
            if (state.casts.isNotEmpty()) {
                AdditionalInfoSection(
                    title = {
                        Text(
                            text = stringResource(R.string.anime_detail_section_actor_character),
                            style = AniPick18ExtraBold.copy(color = AniPickBlack)
                        )
                    },
                    itemList = {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large)),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
                        ) {
                            items(state.casts) { cast ->
                                APCastPairCard(
                                    cast = cast,
                                    onClick = { onAction(InfoAnimeAction.NavigateToActor(cast.voiceActor?.id ?: 0)) }
                                )
                            }
                        }
                    },
                    onNavigateClick = { onAction(InfoAnimeAction.NavigateToCasts(state.animeInfo?.animeId ?: 0)) }
                )
            }
            if (state.series.isNotEmpty()) {
                AdditionalInfoSection(
                    title = {
                        Text(
                            text = stringResource(R.string.anime_detail_section_series),
                            style = AniPick18ExtraBold.copy(color = AniPickBlack)
                        )
                    },
                    itemList = {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large)),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                        ) {
                            items(state.series) { anime ->
                                APAnimeCard(
                                    cardWidth = 128.dp,
                                    imageUrl = anime.coverImageUrl,
                                    title = anime.titleKor ?: anime.titleEng ?: anime.title,
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
                                    onClick = { onAction(InfoAnimeAction.NavigateToAnimeDetail(anime.animeId ?: 0)) }
                                )
                            }
                        }
                    },
                    onNavigateClick = { onAction(InfoAnimeAction.NavigateToSeries(state.animeInfo?.animeId ?: 0, state.animeInfo?.title ?: "")) }
                )
            }
            if (state.recommendations.isNotEmpty()) {
                AdditionalInfoSection(
                    title = {
                        Text(
                            text = stringResource(R.string.anime_detail_section_recommend),
                            style = AniPick18ExtraBold.copy(color = AniPickBlack)
                        )
                    },
                    itemList = {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large)),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                        ) {
                            items(state.recommendations) { anime ->
                                APAnimeCard(
                                    cardWidth = 128.dp,
                                    imageUrl = anime.coverImageUrl,
                                    title = anime.titleKor ?: anime.titleEng ?: anime.title,
                                    maxLine = 2,
                                    onClick = { onAction(InfoAnimeAction.NavigateToAnimeDetail(anime.animeId ?: 0)) }
                                )
                            }
                        }
                    },
                    onNavigateClick = { onAction(InfoAnimeAction.NavigateToRecommend(state.animeInfo?.animeId ?: 0))}
                )
            }
        }
    }
}

@Composable
private fun ReviewTab(
    state: InfoAnimeState,
    onAction: (InfoAnimeAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.padding_medium)),
    ) {
        MyReviewSection(
            state = state,
            onAction = onAction
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacing_extra_large)),
            thickness = dimensionResource(R.dimen.border_width_large),
            color = AniPickSurface
        )
        AnimeReviews(
            state = state,
            onAction = onAction,
        )
    }
}

@Composable
private fun MyReviewSection(
    state: InfoAnimeState,
    onAction: (InfoAnimeAction) -> Unit,
) {
    if (state.myReview.content != null) {
        var showDropDown by rememberSaveable { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            Text(
                text = stringResource(R.string.anime_detail_my_review),
                style = AniPick18ExtraBold.copy(color = AniPickBlack)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = dimensionResource(R.dimen.border_width_thick), color = AniPickGray50, shape = AniPickSmallShape)
                    .padding(dimensionResource(R.dimen.padding_large)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0 until 5) {
                            val rating = state.myReview.rating ?: 0f
                            val ratingInt = rating.toInt()
                            val hasHalfStar = rating.rem(1) != 0f

                            Box(
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.icon_size_medium))
                            ) {
                                Image(
                                    imageVector = StarOutlineIcon,
                                    contentDescription = stringResource(R.string.outline_star_icon),
                                    modifier = Modifier.fillMaxSize(),
                                )
                                when {
                                    i < ratingInt -> {
                                        Image(
                                            imageVector = StarFillIcon,
                                            contentDescription = stringResource(R.string.fill_star_icon),
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    }

                                    i == ratingInt && hasHalfStar -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clipToBounds()
                                                .drawWithContent {
                                                    clipRect(left = 0f, top = 0f, right = size.width / 2, bottom = size.height) {
                                                        this@drawWithContent.drawContent()
                                                    }
                                                }
                                        ) {
                                            Image(
                                                imageVector = StarFillIcon,
                                                contentDescription = stringResource(R.string.fill_star_icon),
                                                modifier = Modifier.fillMaxSize(),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        state.myReview.rating?.let {
                            Text(
                                text = it.toString(),
                                style = AniPick14Normal.copy(color = AniPickGray400),
                                modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small))
                            )
                        }
                    }
                    state.myReview.createdAt?.let {
                        Text(
                            text = it,
                            style = AniPick12Normal.copy(color = AniPickGray100)
                        )
                    }
                }
                state.myReview.content?.let {
                    Text(
                        text = it,
                        style = AniPick16Normal.copy(color = AniPickBlack)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        APAnimationLikeIcon(
                            size = dimensionResource(R.dimen.icon_size_small),
                            isLiked = state.myReview.isLiked,
                            isLikingAnime = state.isLikingAnime,
                            onClick = {
                                onAction(InfoAnimeAction.OnReviewLikeClicked(
                                    reviewId = state.myReview.reviewId ?: -1,
                                    animeId = state.animeInfo?.animeId ?: -1,
                                    isLiked = !state.myReview.isLiked,
                                    callback = {}
                                ))
                            }
                        )
                        state.myReview.likeCount?.let {
                            Text(
                                text = it.toString(),
                                style = AniPick14Normal.copy(color = AniPickGray100)
                            )
                        }
                    }
                    Box {
                        Icon(
                            imageVector = MoreVerticalIcon,
                            contentDescription = stringResource(R.string.more_vert_icon),
                            tint = AniPickGray400,
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.icon_size_medium))
                                .clickable { showDropDown = !showDropDown }
                        )
                        DropdownMenu(
                            expanded = showDropDown,
                            onDismissRequest = { showDropDown = !showDropDown },
                            offset = DpOffset(x = 0.dp, y = 8.dp),
                            shape = AniPickSmallShape,
                            containerColor = AniPickWhite,
                            shadowElevation = 2.dp,
                        ) {
                            Text(
                                text = MyDropdownMenu.DELETE.displayName,
                                style = AniPick14Normal.copy(color = AniPickBlack),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showDropDown = !showDropDown
                                        onAction(InfoAnimeAction.OnReviewDeleteClicked(state.myReview.reviewId ?: 0))
                                    }
                                    .padding(horizontal = dimensionResource(R.dimen.padding_huge), vertical = dimensionResource(R.dimen.padding_medium))
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = dimensionResource(R.dimen.padding_default)),
                                thickness = dimensionResource(R.dimen.border_width_default),
                                color = AniPickSurface
                            )
                            Text(
                                text = MyDropdownMenu.UPDATE.displayName,
                                style = AniPick14Normal.copy(color = AniPickBlack),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showDropDown = !showDropDown
                                        onAction(InfoAnimeAction.NavigateToEditReview(
                                            animeId = state.animeInfo?.animeId ?: 0,
                                            type = FormType.EDIT
                                        ))
                                    }
                                    .padding(horizontal = dimensionResource(R.dimen.padding_huge), vertical = dimensionResource(R.dimen.padding_medium))
                            )
                        }
                    }
                }
            }
        }
    } else {
        var rating by rememberSaveable(state.myReview.rating) { mutableFloatStateOf(state.myReview.rating ?: 0f) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AniPickSurface, AniPickSmallShape)
                    .padding(vertical = dimensionResource(R.dimen.spacing_extra_large)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
            ) {
                Row(
                    modifier = Modifier
                        .pointerInput(state.isAnimeRatingLoading) {
                            if (!state.isAnimeRatingLoading) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        // 드래그 시작 위치에서 별점 계산
                                        updateRatingFromPosition(offset.x, size.width, 5).let {
                                            rating = it
                                        }
                                    },
                                    onDragEnd = {
                                        if (rating != (state.myReview.rating ?: 0f)) {
                                            onAction(
                                                InfoAnimeAction.OnRatingChanged(
                                                    rating = rating,
                                                    onFailure = { rating = state.myReview.rating ?: 0f }
                                                )
                                            )
                                        }
                                    },
                                    onDragCancel = { rating = state.myReview.rating ?: 0f },
                                    onDrag = { change, _ ->
                                        // 드래그 위치에서 별점 계산
                                        updateRatingFromPosition(change.position.x, size.width, 5).let {
                                            rating = it
                                        }
                                    }
                                )
                            }
                        }
                        .padding(vertical = dimensionResource(R.dimen.padding_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until 5) {

                        Box(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.icon_size_extra_large))
                                .pointerInput(state.isAnimeRatingLoading) {
                                    if (!state.isAnimeRatingLoading) {
                                        detectTapGestures { offset ->
                                            val position = offset.x / size.width
                                            val newRating = if (position < 0.5f) {
                                                // 왼쪽 절반 (0.5점)
                                                i + 0.5f
                                            } else {
                                                // 오른쪽 절반 (1.0점)
                                                i + 1.0f
                                            }
                                            if (rating != newRating) {
                                                rating = newRating
                                                onAction(
                                                    InfoAnimeAction.OnRatingChanged(
                                                        rating = rating,
                                                        onFailure = { rating = state.myReview.rating ?: 0f }
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                        ) {
                            val ratingInt = rating.toInt()
                            val hasHalfStar = rating.rem(1) != 0f

                            Image(
                                imageVector = StarOutlineIcon,
                                contentDescription = stringResource(R.string.outline_star_icon),
                                modifier = Modifier.fillMaxSize()
                            )
                            when {
                                i < ratingInt -> {
                                    Image(
                                        imageVector = StarFillIcon,
                                        contentDescription = stringResource(R.string.fill_star_icon),
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                }

                                i == ratingInt && hasHalfStar -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clipToBounds()
                                            .drawWithContent {
                                                // 왼쪽 반쪽만 클리핑
                                                clipRect(left = 0f, top = 0f, right = size.width / 2, bottom = size.height) {
                                                    this@drawWithContent.drawContent()
                                                }
                                            }
                                    ) {
                                        Image(
                                            imageVector = StarFillIcon,
                                            contentDescription = stringResource(R.string.fill_star_icon),
                                            modifier = Modifier.fillMaxSize(),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Text(
                    text = stringResource(R.string.anime_detail_rating_format, rating),
                    style = AniPick20Bold.copy(
                        color = if (rating != 0f) AniPickPoint else AniPickGray100
                    )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(AniPickSmallShape)
                    .background(if (state.myReview.rating == 0f) AniPickGray100 else AniPickPrimary, AniPickSmallShape)
                    .clickable(enabled = state.myReview.rating != 0f && !state.isAnimeRatingLoading) {
                        onAction(InfoAnimeAction.NavigateToEditReview(
                            animeId = state.animeInfo?.animeId ?: 0,
                            type = FormType.CREATE
                        ))
                    }
                    .padding(vertical = dimensionResource(R.dimen.spacing_default)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.anime_detail_review_write_button),
                    style = AniPick16Normal.copy(color = AniPickWhite),
                )
            }
        }
    }
}

@Composable
private fun AnimeReviews(
    state: InfoAnimeState,
    onAction: (InfoAnimeAction) -> Unit,
) {
    var includeSpoiler by rememberSaveable { mutableStateOf(false) }
    var showDropDown by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AniPickSurface)
            .padding(bottom = dimensionResource(R.dimen.padding_medium))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AniPickWhite)
                .padding(horizontal = dimensionResource(R.dimen.padding_large))
                .padding(bottom = dimensionResource(R.dimen.padding_extra_large)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = stringResource(R.string.anime_detail_reviews),
                    style = AniPick24Bold.copy(color = AniPickBlack)
                )
                Text(
                    text = stringResource(R.string.anime_detail_reviews_count, state.reviewCount),
                    style = AniPick14Normal.copy(color = AniPickGray100)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.anime_detail_review_spoiler),
                    style = AniPick16Bold.copy(
                        color = if (includeSpoiler) AniPickSecondary else AniPickGray400
                    )
                )
                APToggleSwitch(
                    checked = includeSpoiler,
                    checkedColor = AniPickSecondary,
                    unCheckedColor = AniPickGray400,
                    onCheckedChange = { includeSpoiler = !includeSpoiler }
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_large))
                .padding(top = dimensionResource(R.dimen.padding_extra_large), bottom = dimensionResource(R.dimen.padding_default))
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                Row(
                    modifier = Modifier
                        .clickable { showDropDown = !showDropDown },
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.reviewSort.displayName,
                        style = AniPick14Normal.copy(color = AniPickGray400)
                    )
                    Icon(
                        imageVector = if (showDropDown) ChevronUpIcon else ChevronDownIcon,
                        contentDescription = stringResource(R.string.chevron_up_down_icon),
                        tint = AniPickGray400,
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_small))
                    )
                }
                DropdownMenu(
                    expanded = showDropDown,
                    onDismissRequest = { showDropDown = false },
                    offset = DpOffset(x = 0.dp, y = 8.dp),
                    shape = AniPickSmallShape,
                    containerColor = AniPickWhite,
                    shadowElevation = 2.dp,
                ) {
                    Text(
                        text = ReviewSortType.LATEST.displayName,
                        style = AniPick14Normal.copy(color = AniPickBlack),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showDropDown = !showDropDown
                                onAction(InfoAnimeAction.OnChangeReviewSortType(ReviewSortType.LATEST))
                            }
                            .padding(dimensionResource(R.dimen.padding_default))
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.padding_default)),
                        thickness = dimensionResource(R.dimen.border_width_default),
                        color = AniPickSurface
                    )
                    Text(
                        text = ReviewSortType.LIKES.displayName,
                        style = AniPick14Normal.copy(color = AniPickBlack),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showDropDown = !showDropDown
                                onAction(InfoAnimeAction.OnChangeReviewSortType(ReviewSortType.LIKES))
                            }
                            .padding(dimensionResource(R.dimen.padding_default))
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.padding_default)),
                        thickness = dimensionResource(R.dimen.border_width_default),
                        color = AniPickSurface
                    )
                    Text(
                        text = ReviewSortType.RATING_DESC.displayName,
                        style = AniPick14Normal.copy(color = AniPickBlack),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showDropDown = !showDropDown
                                onAction(InfoAnimeAction.OnChangeReviewSortType(ReviewSortType.RATING_DESC))
                            }
                            .padding(dimensionResource(R.dimen.padding_default))
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(R.dimen.padding_default)),
                        thickness = dimensionResource(R.dimen.border_width_default),
                        color = AniPickSurface
                    )
                    Text(
                        text = ReviewSortType.RATING_ASC.displayName,
                        style = AniPick14Normal.copy(color = AniPickBlack),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showDropDown = !showDropDown
                                onAction(InfoAnimeAction.OnChangeReviewSortType(ReviewSortType.RATING_ASC))
                            }
                            .padding(dimensionResource(R.dimen.padding_default))
                    )
                }
            }
        }
        val filteredReviews = state.reviews.filter { !it.isSpoiler || includeSpoiler }
        if (filteredReviews.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_large)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
            ) {
                filteredReviews.forEach { review ->
                    key(review.reviewId) {
                        APReviewCard(
                            review = review,
                            onClickEdit = { animeId, reviewId ->
                                onAction(InfoAnimeAction.NavigateToEditReview(state.animeInfo?.animeId ?: -1, FormType.EDIT))
                            },
                            onClickDelete = { reviewId ->
                                onAction(InfoAnimeAction.OnReviewDeleteClicked(reviewId))
                            },
                            onCLickReport = { reviewId ->
                                onAction(InfoAnimeAction.OnReviewReportClicked(reviewId))
                            },
                            onClickBlock = { userId ->
                                onAction(InfoAnimeAction.OnUserBlockClicked(userId))
                            },
                            onClickLiked = { reviewId, animeId, isLiked, result ->
                                onAction(InfoAnimeAction.OnReviewLikeClicked(reviewId, state.animeInfo?.animeId ?: -1, isLiked, result))
                            },
                        )
                    }
                }
            }
        } else {
            APEmptyContent(
                modifier = Modifier
                    .fillMaxWidth(),
                comment = stringResource(R.string.anime_detail_empty_reviews)
            )
        }
        if (state.isLoadingMoreReviews) {
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