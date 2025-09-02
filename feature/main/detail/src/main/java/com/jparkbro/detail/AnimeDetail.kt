package com.jparkbro.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.jparkbro.detail.component.TitleFavorite
import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.common.FormType
import com.jparkbro.model.common.MyDropdownMenu
import com.jparkbro.model.common.WatchStatus
import com.jparkbro.model.detail.DetailActor
import com.jparkbro.model.detail.DetailInfo
import com.jparkbro.model.detail.DetailMyReview
import com.jparkbro.model.detail.DetailReviewItem
import com.jparkbro.model.detail.DetailSeries
import com.jparkbro.model.detail.ReviewDetailResponse
import com.jparkbro.model.detail.ReviewSort
import com.jparkbro.ui.APCardItem
import com.jparkbro.ui.APDialog
import com.jparkbro.ui.APReviewItem
import com.jparkbro.ui.APSnackBar
import com.jparkbro.ui.APToggleSwitch
import com.jparkbro.ui.DialogData
import com.jparkbro.ui.R
import com.jparkbro.ui.SnackBarData
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.updateRatingFromPosition
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun DetailAnime(
    onNavigateBack: () -> Unit,
    onNavigateToReviewForm: (Int, Int?, FormType) -> Unit,
    onNavigateToStudioDetail: (Int) -> Unit,
    onNavigateToAnimeActors: (Int) -> Unit,
    onCheckReviewRefresh: () -> Boolean,
    onClearReviewRefresh: () -> Unit,
    onStatusRefresh: () -> Unit,
    viewModel: DetailAnimeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val tabIndex by viewModel.tabIndex.collectAsState()

    val sort by viewModel.sort.collectAsState()
    val showSortDropdown by viewModel.showSortDropdown.collectAsState()
    val includeSpoiler by viewModel.includeSpoiler.collectAsState()
    val snackBarData by viewModel.snackBarData.collectAsState()
    val dialogData by viewModel.dialogData.collectAsState()

    val detailInfo by viewModel.detailInfo.collectAsState()
    val actors by viewModel.actors.collectAsState()
    val series by viewModel.series.collectAsState()
    val recommendations by viewModel.recommendations.collectAsState()

    val myReview by viewModel.myReview.collectAsState()
    val reviewResponse by viewModel.reviewResponse.collectAsState()
    val reviewItems by viewModel.reviewItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isReviewLikedLoading by viewModel.isReviewLikedLoading.collectAsState()

    val isLikeLoading by viewModel.isLikeLoading.collectAsState()
    val isChangeStatusLoading by viewModel.isChangeStatusLoading.collectAsState()
    val isAnimeRatingLoading by viewModel.isAnimeRatingLoading.collectAsState()

    DetailAnime(
        uiState = uiState,
        tabIndex = tabIndex,
        sort = sort,
        showSortDropdown = showSortDropdown,
        includeSpoiler = includeSpoiler,
        dialogData = dialogData,
        onChangeDialogData = viewModel::updateDialogData,
        snackBarData = snackBarData,
        detailInfo = detailInfo,
        actors = actors,
        series = series,
        recommendations = recommendations,
        myReview = myReview,
        reviewResponse = reviewResponse,
        reviewItems = reviewItems,
        isLoading = isLoading,
        isReviewLikedLoading = isReviewLikedLoading,
        isLikeLoading = isLikeLoading,
        isChangeStatusLoading = isChangeStatusLoading,
        isAnimeRatingLoading = isAnimeRatingLoading,
        onShareAnimeLink = viewModel::copyAnimeLink,
        onChangeTabIndex = viewModel::updateTabIndex,
        onChangeIncludeSpoiler = viewModel::toggleIncludeSpoiler,
        onChangeSort = viewModel::updateSort,
        onChangeSortDropdown = viewModel::changeDropdownState,
        onChangeSnackBarData = viewModel::updateSnackBarData,
        onRateAnime = viewModel::rateAnime,
        onLikeAnime = viewModel::likeAnime,
        onSetAnimeStatus = viewModel::changeAnimeStatus,
        onLoadMoreReviews = viewModel::getAnimeReviews,
        onChangeLikeState = viewModel::updateLikeState,
        onDeleteReview = viewModel::deleteReview,
        onReportReview = viewModel::reportReview,
        onBlockUser = viewModel::blockUser,
        onNavigateBack = onNavigateBack,
        onNavigateToReviewForm = onNavigateToReviewForm,
        onNavigateToStudioDetail = onNavigateToStudioDetail,
        onNavigateToAnimeActors = onNavigateToAnimeActors,
        onCheckReviewRefresh = onCheckReviewRefresh,
        onClearReviewRefresh = onClearReviewRefresh,
        onStatusRefresh = onStatusRefresh,
        onRefreshAllData = viewModel::getInitData
    )
}

@Composable
private fun DetailAnime(
    uiState: DetailUiState = DetailUiState.Loading,
    tabIndex: DetailTab = DetailTab.ANIME_INFO,
    sort: ReviewSort = ReviewSort.LATEST,
    showSortDropdown: Boolean = false,
    includeSpoiler: Boolean = false,
    dialogData: DialogData? = null,
    onChangeDialogData: (DialogData?) -> Unit,
    snackBarData: SnackBarData? = null,
    detailInfo: DetailInfo?,
    actors: List<DetailActor>,
    series: List<DetailSeries>,
    recommendations: List<DefaultAnime>,
    myReview: DetailMyReview? = null,
    reviewResponse: ReviewDetailResponse? = null,
    reviewItems: List<DetailReviewItem> = emptyList(),
    isLoading: Boolean = false,
    isReviewLikedLoading: Boolean = false,
    isLikeLoading: Boolean,
    isChangeStatusLoading: Boolean,
    isAnimeRatingLoading: Boolean,
    onShareAnimeLink: () -> Unit,
    onChangeTabIndex: (DetailTab) -> Unit,
    onChangeIncludeSpoiler: () -> Unit,
    onChangeSort: (ReviewSort) -> Unit,
    onChangeSortDropdown: () -> Unit,
    onChangeSnackBarData: (SnackBarData?) -> Unit,
    onRateAnime: (rating: Float, onResult: (Boolean) -> Unit) -> Unit,
    onLikeAnime: (Boolean) -> Unit,
    onSetAnimeStatus: (WatchStatus) -> Unit,
    onLoadMoreReviews: (Int?) -> Unit = {},
    onChangeLikeState: (Boolean, Int, (Boolean) -> Unit) -> Unit,
    onDeleteReview: (Int) -> Unit,
    onReportReview: (Int, String) -> Unit,
    onBlockUser: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToReviewForm: (Int, Int?, FormType) -> Unit,
    onNavigateToStudioDetail: (Int) -> Unit,
    onNavigateToAnimeActors: (Int) -> Unit,
    onCheckReviewRefresh: () -> Boolean,
    onClearReviewRefresh: () -> Unit,
    onStatusRefresh: () -> Unit,
    onRefreshAllData: () -> Unit,
) {
    LaunchedEffect(Unit) {
        val isUpdate = onCheckReviewRefresh()
        if (isUpdate) {
            onRefreshAllData()
            onClearReviewRefresh()
        }
    }

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

    LaunchedEffect(lazyListState, reviewItems.size) {
        snapshotFlow { lazyListState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 2
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !isLoading && reviewItems.isNotEmpty()) {
                    onLoadMoreReviews(reviewResponse?.cursor?.lastId)
                }
            }
    }

    when (uiState) {
        is DetailUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is DetailUiState.Success -> {

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(APColors.White)
                    .windowInsetsPadding(
                        WindowInsets(
                            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                        )
                    )
            ) {
                item {
                    /** Expanded Header */
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                        ) {
                            Box {
                                if (detailInfo?.bannerImageUrl != null) {
                                    AsyncImage(
                                        model = detailInfo.bannerImageUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp)
                                            .background(Color(0xFFF8F9FD))
                                    )
                                }
                                Icon(
                                    painter = painterResource(R.drawable.ic_border_chevron_left),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(top = 54.dp, start = 20.dp)
                                        .size(24.dp)
                                        .clickable { onNavigateBack() }
                                )
                                AsyncImage(
                                    model = detailInfo?.coverImageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(top = 44.dp, end = 20.dp)
                                        .size(width = 133.dp, height = 153.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .background(APColors.White)
                                    .padding(horizontal = 20.dp, vertical = 24.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TitleFavorite(
                                        title = "${detailInfo?.title}",
                                        isLiked = detailInfo?.isLiked == true,
                                        onChangeLikeState = { onLikeAnime(detailInfo?.isLiked == true) },
                                        enabled = !isLikeLoading,
                                        modifier = Modifier
                                            .weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Icon(
                                        painter = painterResource(R.drawable.ic_share),
                                        contentDescription = null,
                                        tint = Color.Unspecified,
                                        modifier = Modifier
                                            .size(33.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                onShareAnimeLink()
                                                onChangeSnackBarData(SnackBarData(text = "링크가 복사되었습니다."))
                                              },
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 32.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_star_fill),
                                        contentDescription = null,
                                        tint = Color.Unspecified,
                                        modifier = Modifier
                                            .size(19.dp)
                                    )
                                    Text(
                                        text = if (detailInfo?.averageRating == null) "0.0" else "${detailInfo.averageRating}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W600,
                                        color = APColors.Point
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    val watchStatus = detailInfo?.watchStatus
                                    Text(
                                        text = "볼 애니",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W500,
                                        color = if (watchStatus == WatchStatus.WATCHLIST) APColors.White else APColors.Black,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable(enabled = !isChangeStatusLoading) {
                                                onSetAnimeStatus(WatchStatus.WATCHLIST)
                                                onStatusRefresh()
                                            }
                                            .background(
                                                if (watchStatus == WatchStatus.WATCHLIST) APColors.Primary else APColors.LightGray,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(vertical = 7.dp)
                                    )
                                    Text(
                                        text = "보는 중",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W500,
                                        color = if (watchStatus == WatchStatus.WATCHING) APColors.White else APColors.Black,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable(enabled = !isChangeStatusLoading) {
                                                onSetAnimeStatus(WatchStatus.WATCHING)
                                                onStatusRefresh()
                                            }
                                            .background(
                                                if (watchStatus == WatchStatus.WATCHING) APColors.Primary else APColors.LightGray,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(vertical = 7.dp)
                                    )
                                    Text(
                                        text = "다 본 애니",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W500,
                                        color = if (watchStatus == WatchStatus.FINISHED) APColors.White else APColors.Black,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable(enabled = !isChangeStatusLoading) {
                                                onSetAnimeStatus(WatchStatus.FINISHED)
                                                onStatusRefresh()
                                            }
                                            .background(
                                                if (watchStatus == WatchStatus.FINISHED) APColors.Primary else APColors.LightGray,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .padding(vertical = 7.dp)
                                    )
                                }
                            }
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 3.dp,
                                color = APColors.LightGray
                            )
                        }
                    }
                }
                stickyHeader {
                    /** Collapse Header */
                    if (showStickyHeader) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(APColors.White)
                                .padding(top = 24.dp)
                                .padding(horizontal = 20.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_chevron_left),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.CenterStart)
                                    .clickable { onNavigateBack() }
                            )
                            TitleFavorite(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(horizontal = 48.dp),
                                title = "${detailInfo?.title}",
                                isLiked = detailInfo?.isLiked == true,
                                onChangeLikeState = { onLikeAnime(detailInfo?.isLiked == true) },
                                enabled = !isLikeLoading
                            )
                        }
                    }
                    TabRow(
                        selectedTabIndex = when (tabIndex) {
                            DetailTab.ANIME_INFO -> 0
                            DetailTab.REVIEWS -> 1
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(APColors.White)
                            .padding(horizontal = 20.dp),
                        containerColor = APColors.White,
                        indicator = { tabPositions ->
                            TabRowDefaults.PrimaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(
                                    tabPositions[when (tabIndex) {
                                        DetailTab.ANIME_INFO -> 0
                                        DetailTab.REVIEWS -> 1
                                    }]
                                ),
                                width = tabPositions[when (tabIndex) {
                                    DetailTab.ANIME_INFO -> 0
                                    DetailTab.REVIEWS -> 1
                                }].width,
                                height = 2.dp,
                                color = APColors.LightGray
                            )
                        },
                        divider = {}
                    ) {
                        DetailTab.entries.forEachIndexed { index, title ->
                            val isSelected = tabIndex == title
                            Tab(
                                selected = isSelected,
                                onClick = { onChangeTabIndex(title) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = when (title) {
                                        DetailTab.ANIME_INFO -> "작품 정보"
                                        DetailTab.REVIEWS -> "리뷰(${detailInfo?.reviewCount}개)"
                                    },
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W500,
                                    color = if (isSelected) APColors.Black else APColors.Gray
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(13.dp))
                    when (tabIndex) {
                        DetailTab.ANIME_INFO -> AnimeInfo(
                            detailInfo = detailInfo,
                            actors = actors,
                            series = series,
                            recommendations = recommendations,
                            onNavigateToStudioDetail = onNavigateToStudioDetail,
                            onNavigateToAnimeActors = onNavigateToAnimeActors,
                        )

                        DetailTab.REVIEWS -> AnimeReview(
                            sort = sort,
                            showSortDropdown = showSortDropdown,
                            includeSpoiler = includeSpoiler,
                            detailInfo = detailInfo,
                            onChangeDialogData = onChangeDialogData,
                            myReview = myReview,
                            reviewResponse = reviewResponse,
                            reviewItems = reviewItems,
                            isAnimeRatingLoading = isAnimeRatingLoading,
                            isLoading = isLoading,
                            isReviewLikedLoading = isReviewLikedLoading,
                            onChangeIncludeSpoiler = onChangeIncludeSpoiler,
                            onChangeSort = onChangeSort,
                            onChangeSortDropdown = onChangeSortDropdown,
                            onChangeSnackBarData = onChangeSnackBarData,
                            onRateAnime = onRateAnime,
                            onNavigateToReviewForm = onNavigateToReviewForm,
                            onChangeLikeState = onChangeLikeState,
                            onDeleteReview = onDeleteReview,
                            onReportReview = onReportReview,
                            onBlockUser = onBlockUser,
                        )
                    }
                }
            }
        }

        is DetailUiState.Error -> {
            Text(
                text = "error"
            )
        } // TODO
    }

    snackBarData?.let {
        APSnackBar(
            snackBarData = it,
            visible = true,
            onDismiss = { onChangeSnackBarData(null) }
        )
    }

    dialogData?.let {
        APDialog(
            title = it.title,
            subTitle = it.subTitle,
            content = it.content,
            dismiss = it.dismiss,
            confirm = it.confirm,
            onDismiss = it.onDismiss,
            onConfirm = it.onConfirm
        )
    }
}

@Composable
private fun AnimeInfo(
    detailInfo: DetailInfo?,
    actors: List<DetailActor>,
    series: List<DetailSeries>,
    recommendations: List<DefaultAnime>,
    onNavigateToStudioDetail: (Int) -> Unit,
    onNavigateToAnimeActors: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var isTextOverflowing by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "${detailInfo?.description}",
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = APColors.Black,
                lineHeight = (1.4).em,
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    isTextOverflowing = textLayoutResult.hasVisualOverflow
                }
            )
            if (isTextOverflowing || expanded) {
                Row(
                    modifier = Modifier
                        .clickable { expanded = !expanded },
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (expanded) "접기" else "더보기",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.Primary
                    )
                    Icon(
                        painter = painterResource(if (expanded) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down),
                        contentDescription = null,
                        tint = APColors.Primary,
                        modifier = Modifier
                            .size(15.dp)
                    )
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 19.dp, bottom = 33.dp), thickness = 3.dp, color = APColors.LightGray)
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "타입",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black
                )
                Text(
                    text = "${detailInfo?.type}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.TextGray
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "장르",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    detailInfo?.genres?.forEach { genre ->
                        Text(
                            text = genre.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.Primary,
                            modifier = Modifier
                                .background(APColors.Primary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "방영 시기",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${detailInfo?.status}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.Black,
                        modifier = Modifier
                            .background(APColors.LightGray, CircleShape)
                            .padding(horizontal = 10.dp, vertical = 2.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${detailInfo?.airDate}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.TextGray
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "에피소드",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black
                )
                Text(
                    text = "${detailInfo?.episode}회차",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.TextGray
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "연령 등급",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black
                )
                Text(
                    text = "${detailInfo?.age}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.TextGray
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "제작사",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    detailInfo?.studios?.forEach { studio ->
                        Text(
                            text = "${studio.name}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.Secondary,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .clickable { onNavigateToStudioDetail(studio.studioId) }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(48.dp)
        ) {
            /** 캐릭터, 성우 */
            if (actors.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    ClickableSectionTitle(
                        title = "캐릭터/성우진",
                        onClick = { onNavigateToAnimeActors(detailInfo?.animeId ?: 0) }
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(actors) { actor ->
                            Row(
                                modifier = Modifier
                                    .background(APColors.Surface, RoundedCornerShape(8.dp)),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Column {
                                    AsyncImage(
                                        model = actor.character.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(91.dp)
                                            .height(95.dp)
                                            .clip(RoundedCornerShape(topStart = 8.dp))
                                            .background(APColors.Gray, RoundedCornerShape(topStart = 8.dp)),
//                                        contentScale = ContentScale.Crop
                                    )
                                    Box(
                                        modifier = Modifier
                                            .height(46.dp)
                                            .padding(start = 8.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            text = "${actor.character.name}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            color = APColors.Black
                                        )
                                    }
                                }
                                Column {
                                    AsyncImage(
                                        model = actor.voiceActor.imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(91.dp)
                                            .height(95.dp)
                                            .clip(RoundedCornerShape(topEnd = 8.dp))
                                            .background(APColors.Gray, RoundedCornerShape(topEnd = 8.dp)),
//                                        contentScale = ContentScale.Crop
                                    )
                                    Box(
                                        modifier = Modifier
                                            .height(46.dp)
                                            .padding(start = 8.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            text = "${actor.voiceActor.name}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                            color = APColors.Black
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            /** 시리즈 정보 */
            if (series.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    ClickableSectionTitle(
                        title = "시리즈 정보",
                        onClick = {}
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(series) { anime ->
                            APCardItem(
                                title = "${anime.title}",
                                imageUrl = anime.coverImageUrl,
                                description = {
                                    Text(
                                        text = anime.airDate,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.W500,
                                        color = APColors.TextGray
                                    )
                                },
                                cardWidth = 115.dp,
                                cardHeight = 162.dp,
                                fontSize = 14.sp,
                                maxLine = 2,
                                onClick = { }
                            )
                        }
                    }
                }
            }
            /** 추천 */
            if (recommendations.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    ClickableSectionTitle(
                        title = "함께 볼 만한 작품",
                        onClick = {}
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(recommendations) { anime ->
                            APCardItem(
                                title = "${anime.title}",
                                imageUrl = anime.coverImageUrl,
                                cardWidth = 115.dp,
                                cardHeight = 162.dp,
                                fontSize = 14.sp,
                                maxLine = 1,
                                onClick = { }
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun AnimeReview(
    sort: ReviewSort = ReviewSort.LATEST,
    showSortDropdown: Boolean = false,
    includeSpoiler: Boolean = false,
    detailInfo: DetailInfo?,
    onChangeDialogData: (DialogData?) -> Unit,
    myReview: DetailMyReview? = null,
    reviewResponse: ReviewDetailResponse? = null,
    reviewItems: List<DetailReviewItem> = emptyList(),
    isAnimeRatingLoading: Boolean = false,
    isLoading: Boolean = false,
    isReviewLikedLoading: Boolean = false,
    onChangeIncludeSpoiler: () -> Unit,
    onChangeSort: (ReviewSort) -> Unit,
    onChangeSortDropdown: () -> Unit,
    onChangeSnackBarData: (SnackBarData?) -> Unit,
    onRateAnime: (rating: Float, onResult: (Boolean) -> Unit) -> Unit,
    onNavigateToReviewForm: (Int, Int?, FormType) -> Unit,
    onChangeLikeState: (Boolean, Int, (Boolean) -> Unit) -> Unit,
    onDeleteReview: (Int) -> Unit,
    onReportReview: (Int, String) -> Unit,
    onBlockUser: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MyReviewContainer(
            detailInfo = detailInfo,
            myReview = myReview,
            onRateAnime = onRateAnime,
            isAnimeRatingLoading = isAnimeRatingLoading,
            onChangeSnackBarData = onChangeSnackBarData,
            onNavigateToReviewForm = onNavigateToReviewForm,
        )
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 5.dp, color = APColors.Surface)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "리뷰",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    color = APColors.Black
                )
                Text(
                    text = "${detailInfo?.reviewCount}개",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Gray
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "스포일러",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W600,
                    color = if (includeSpoiler) APColors.Secondary else APColors.TextGray
                )
                APToggleSwitch(
                    checked = includeSpoiler,
                    checkedColor = APColors.Secondary,
                    unCheckedColor = APColors.TextGray,
                    onCheckedChange = { onChangeIncludeSpoiler() }
                )
            }
        }
        Column(
            modifier = Modifier
                .background(APColors.Surface)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onChangeSortDropdown() },
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = sort.displayName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.TextGray
                        )
                        Icon(
                            painter = painterResource(if (showSortDropdown) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down),
                            contentDescription = null,
                            tint = APColors.TextGray,
                            modifier = Modifier
                                .size(15.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = showSortDropdown,
                        onDismissRequest = { onChangeSortDropdown() },
                        offset = DpOffset(x = 0.dp, y = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        containerColor = APColors.White,
                        shadowElevation = 2.dp,
                    ) {
                        Text(
                            text = ReviewSort.LATEST.displayName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = if (sort == ReviewSort.LATEST) APColors.Black else APColors.TextGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    onChangeSortDropdown()
                                    onChangeSort(ReviewSort.LATEST)
                                }
                                .padding(14.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            thickness = 1.dp,
                            color = APColors.Surface
                        )
                        Text(
                            text = ReviewSort.LIKES.displayName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = if (sort == ReviewSort.LIKES) APColors.Black else APColors.TextGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    onChangeSortDropdown()
                                    onChangeSort(ReviewSort.LIKES)
                                }
                                .padding(14.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            thickness = 1.dp,
                            color = APColors.Surface
                        )
                        Text(
                            text = ReviewSort.RATING_DESC.displayName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = if (sort == ReviewSort.RATING_DESC) APColors.Black else APColors.TextGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    onChangeSortDropdown()
                                    onChangeSort(ReviewSort.RATING_DESC)
                                }
                                .padding(14.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            thickness = 1.dp,
                            color = APColors.Surface
                        )
                        Text(
                            text = ReviewSort.RATING_ASC.displayName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = if (sort == ReviewSort.RATING_ASC) APColors.Black else APColors.TextGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    onChangeSortDropdown()
                                    onChangeSort(ReviewSort.RATING_ASC)
                                }
                                .padding(14.dp)
                        )
                    }
                }
            }
            reviewItems.filter { !it.isSpoiler || includeSpoiler }.forEach { review ->
                val radioOptions = listOf("스포일러", "편파적인 언행", "욕설 및 비하", "홍보성 및 영리 목적", "음란성 및 선정성")
                var selectedOption by remember { mutableStateOf(radioOptions[0]) }
                APReviewItem(
                    reviewItem = review,
                    onClickUpdate = { onNavigateToReviewForm(detailInfo?.animeId ?: -1, review.reviewId, FormType.EDIT) },
                    onClickDelete = { onDeleteReview(review.reviewId) },
                    onClickBlock = {
                        onChangeDialogData(DialogData(
                            title = "사용자를 차단하시겠습니까?",
                            subTitle = "차단한 사용자의 리뷰, 커뮤니티 게시글 및\n댓글 등 모든 컨텐츠가 노출되지 않게 됩니다.",
                            dismiss = "취소",
                            confirm = "확인",
                            onDismiss = { onChangeDialogData(null) },
                            onConfirm = {
                                onBlockUser(review.userId ?: -1)
                                onChangeDialogData(null)
                            },
                        ))
                    },
                    onCLickReport = {
                        onChangeDialogData(DialogData(
                            title = "리뷰를 신고하시겠습니까?",
                            subTitle = "신고 시, 리뷰 검토 후 처리됩니다.",
                            dismiss = "취소",
                            confirm = "다음",
                            onDismiss = { onChangeDialogData(null) },
                            onConfirm = { onChangeDialogData(
                                DialogData(
                                    title = "신고하는 사유를 선택해주세요.",
                                    subTitle = "신고 시, 검토 후 처리되어요.",
                                    content = {
                                        FlowRow(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 20.dp, vertical = 16.dp)
                                                .selectableGroup(),
                                            maxItemsInEachRow = 2,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                            radioOptions.forEach { text ->
                                                Row(
                                                    Modifier
                                                        .weight(1f)
                                                        .height(22.dp)
                                                        .selectable(
                                                            selected = (text == selectedOption),
                                                            onClick = { selectedOption = text },
                                                            role = Role.RadioButton
                                                        ),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    RadioButton(
                                                        selected = (text == selectedOption),
                                                        onClick = { selectedOption = text },
                                                        colors = RadioButtonDefaults.colors(
                                                            selectedColor = APColors.Point,
                                                            unselectedColor = APColors.Gray
                                                        )
                                                    )
                                                    Text(
                                                        text = text,
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.W500,
                                                        color = APColors.Black,
                                                        modifier = Modifier
                                                            .padding(start = 8.dp)
                                                    )
                                                }
                                            }
                                        }
                                    },
                                    dismiss = "취소",
                                    confirm = "신고하기",
                                    onDismiss = { onChangeDialogData(null) },
                                    onConfirm = {
                                        // TODO callback
                                        onReportReview(review.reviewId, selectedOption)
                                        onChangeDialogData(null)
                                    },
                                )
                            )},
                        ))
                    },
                    isLikedLoading = isReviewLikedLoading,
                    onClickLiked = { isLiked, callBack ->
                        onChangeLikeState(isLiked, review.reviewId) { result ->
                            callBack(result)
                        }
                    },
                )
            }
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun ClickableSectionTitle(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.W700,
            color = APColors.Black
        )
        Icon(
            painter = painterResource(R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = APColors.Gray,
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Composable
private fun MyReviewContainer(
    detailInfo: DetailInfo?,
    myReview: DetailMyReview?,
    onRateAnime: (rating: Float, onResult: (Boolean) -> Unit) -> Unit,
    isAnimeRatingLoading: Boolean = false,
    onChangeSnackBarData: (SnackBarData?) -> Unit,
    onNavigateToReviewForm: (Int, Int?, FormType) -> Unit,
) {
    val initRating = myReview?.rating ?: 0f

    if (myReview?.reviewId == null || myReview.content == null) {
        /** 상세 Review Content 없을 때 */
        var rating by remember { mutableFloatStateOf(initRating) }

        Column {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .background(APColors.Surface, RoundedCornerShape(8.dp))
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .pointerInput(isAnimeRatingLoading) {
                            /* 별 점 드래그 */
                            if (!isAnimeRatingLoading) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        // 드래그 시작 위치에서 별점 계산
                                        updateRatingFromPosition(offset.x, size.width, 5).let {
                                            rating = it
                                        }
                                    },
                                    onDragEnd = {
                                        onRateAnime(rating) { result ->
                                            if (result) {
                                                onChangeSnackBarData(SnackBarData(text = "리뷰가 성공적으로 작성되었습니다!"))
                                            } else {
                                                rating = initRating
                                            }
                                        }
                                    },
                                    onDragCancel = { rating = initRating },
                                    onDrag = { change, _ ->
                                        // 드래그 위치에서 별점 계산
                                        updateRatingFromPosition(change.position.x, size.width, 5).let {
                                            rating = it
                                        }
                                    }
                                )
                            }
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until 5) {
                        Box(
                            modifier = Modifier
                                .size(31.dp)
                                .pointerInput(isAnimeRatingLoading) {
                                    /* 별 점 클릭 */
                                    if (!isAnimeRatingLoading) {
                                        detectTapGestures { offset ->
                                            // 별의 왼쪽 절반 또는 오른쪽 절반인지 확인
                                            val position = offset.x / size.width
                                            val newRating = if (position < 0.5f) {
                                                // 왼쪽 절반 (0.5점)
                                                i + 0.5f
                                            } else {
                                                // 오른쪽 절반 (1.0점)
                                                i + 1.0f
                                            }
                                            rating = newRating
                                            onRateAnime(rating) { result ->
                                                if (result) {
                                                    onChangeSnackBarData(SnackBarData(text = "리뷰가 성공적으로 작성되었습니다!"))
                                                } else {
                                                    rating = initRating
                                                }
                                            }
                                        }
                                    }
                                }
                        ) {
                            /* 빈 별 */
                            Image(
                                painter = painterResource(R.drawable.ic_star_outline),
                                contentDescription = "별점",
                                modifier = Modifier.fillMaxSize(),
                                colorFilter = null
                            )

                            /* 별 채우기 */
                            when {
                                i < rating.toInt() -> {
                                    Image(
                                        painter = painterResource(R.drawable.ic_star_fill),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        colorFilter = null,
                                    )
                                }

                                i == rating.toInt() && rating % 1 != 0f -> {
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
                                            painter = painterResource(R.drawable.ic_star_fill),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            colorFilter = null,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Text(
                    text = "(${rating}/5.0)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = if (rating != 0f) APColors.Point else APColors.Gray
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                onClick = { onNavigateToReviewForm(detailInfo?.animeId ?: -1, null, FormType.CREATE) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(51.dp),
                enabled = rating != 0f && !isAnimeRatingLoading,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.textButtonColors(
                    containerColor = APColors.Primary,
                    disabledContainerColor = APColors.Gray,
                    contentColor = APColors.White
                )
            ) {
                Text(
                    text = "상세 리뷰 작성하기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                )
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    } else {
        /** 상세 Review Content 있을 때 */
        var showDropDown by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Text(
                text = "내 리뷰",
                fontSize = 18.sp,
                fontWeight = FontWeight.W700,
                color = APColors.Black
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 2.dp, color = APColors.LightGray, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 18.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_star_outline),
                                    contentDescription = "별점",
                                    modifier = Modifier.fillMaxSize(),
                                    colorFilter = null
                                )
                                when {
                                    i < initRating.toInt() -> {
                                        Image(
                                            painter = painterResource(R.drawable.ic_star_fill),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            colorFilter = null,
                                        )
                                    }

                                    i == initRating.toInt() && initRating % 1 != 0f -> {
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
                                                painter = painterResource(R.drawable.ic_star_fill),
                                                contentDescription = null,
                                                modifier = Modifier.fillMaxSize(),
                                                colorFilter = null,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Text(
                            text = "$initRating",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.TextGray,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                    Text(
                        text = "${myReview.createdAt}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.Gray
                    )
                }
                Text(
                    text = "${myReview.content}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_favorite_off),
                            contentDescription = null,
                            tint = APColors.Gray,
                            modifier = Modifier
                                .size(16.dp)
                        )
                        Text(
                            text = "${myReview.likeCount}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.Gray
                        )
                    }
                    Box {
                        Icon(
                            painter = painterResource(R.drawable.ic_more_vertical),
                            contentDescription = null,
                            tint = APColors.TextGray,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { showDropDown = !showDropDown }
                        )
                        DropdownMenu(
                            expanded = showDropDown,
                            onDismissRequest = { showDropDown = !showDropDown },
                            offset = DpOffset(x = 0.dp, y = 8.dp),
                            shape = RoundedCornerShape(8.dp),
                            containerColor = APColors.White,
                            shadowElevation = 2.dp,
                        ) {
                            Text(
                                text = MyDropdownMenu.DELETE.displayName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        showDropDown = !showDropDown
                                        // TODO
                                    }
                                    .padding(horizontal = 33.dp, vertical = 14.dp)
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                                thickness = 1.dp,
                                color = APColors.Surface
                            )
                            Text(
                                text = MyDropdownMenu.UPDATE.displayName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        showDropDown = !showDropDown
                                        onNavigateToReviewForm(detailInfo?.animeId ?: -1, myReview.reviewId, FormType.EDIT)
                                    }
                                    .padding(horizontal = 33.dp, vertical = 14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}