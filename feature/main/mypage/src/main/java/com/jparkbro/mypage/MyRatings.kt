package com.jparkbro.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.model.common.FormType
import com.jparkbro.model.detail.ReviewSort
import com.jparkbro.model.mypage.ContentType
import com.jparkbro.model.mypage.MyReviewItem
import com.jparkbro.model.mypage.MyReviewsResponse
import com.jparkbro.ui.APConfirmDialog
import com.jparkbro.ui.APEmptyContent
import com.jparkbro.ui.APReviewItem
import com.jparkbro.ui.APSnackBar
import com.jparkbro.ui.APTitledBackTopAppBar
import com.jparkbro.ui.APToggleSwitch
import com.jparkbro.ui.DialogData
import com.jparkbro.ui.R
import com.jparkbro.ui.SnackBarData
import com.jparkbro.ui.theme.APColors
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun MyRatings(
    onNavigateBack: () -> Unit,
    onNavigateToReviewForm: (Int, Int, FormType) -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    viewModel: MyRatingsViewModel = hiltViewModel()
) {
    val reviews by viewModel.reviews.collectAsState()
    val reviewResponse by viewModel.reviewResponse.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLikedLoading by viewModel.isLikedLoading.collectAsState()

    val sort by viewModel.sort.collectAsState()
    val dialogData by viewModel.dialogData.collectAsState()
    val snackBarData by viewModel.snackBarData.collectAsState()
    val isReviewOnly by viewModel.isReviewOnly.collectAsState()
    val showSortDropdown by viewModel.showSortDropdown.collectAsState()

    MyRatings(
        reviews = reviews,
        reviewResponse = reviewResponse,
        isLoading = isLoading,
        isLikedLoading = isLikedLoading,
        onLoadMoreData = viewModel::getMyReviews,
        sort = sort,
        dialogData = dialogData,
        onChangeDialogData = viewModel::updateDialogData,
        snackBarData = snackBarData,
        onChangeSnackBarData = viewModel::updateSnackBarData,
        isReviewOnly = isReviewOnly,
        showSortDropdown = showSortDropdown,
        onChangeSort = viewModel::updateSort,
        onChangeOnlyReview = viewModel::toggleReviewOnly,
        onChangeShowSortDropdown = viewModel::changeDropdownState,
        onChangeLikeState = viewModel::updateLikeState,
        onDeleteReview = viewModel::deleteReview,
        onNavigateBack = onNavigateBack,
        onNavigateToReviewForm = onNavigateToReviewForm,
        onNavigateToAnimeDetail = onNavigateToAnimeDetail,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyRatings(
    reviews: List<MyReviewItem> = emptyList(),
    reviewResponse: MyReviewsResponse? = null,
    isLoading: Boolean = false,
    isLikedLoading: Boolean = false,
    onLoadMoreData: (Int?) -> Unit = {},
    sort: ReviewSort = ReviewSort.LATEST,
    dialogData: DialogData? = null,
    onChangeDialogData: (DialogData?) -> Unit,
    snackBarData: SnackBarData? = null,
    onChangeSnackBarData: (SnackBarData?) -> Unit,
    isReviewOnly: Boolean = false,
    showSortDropdown: Boolean = false,
    onChangeSort: (ReviewSort) -> Unit,
    onChangeOnlyReview: () -> Unit,
    onChangeShowSortDropdown: () -> Unit,
    onChangeLikeState: (Boolean, Int, (Boolean) -> Unit) -> Unit,
    onDeleteReview: (Int) -> Unit,
    onNavigateBack: () -> Unit = {},
    onNavigateToReviewForm: (Int, Int, FormType) -> Unit = { _, _, _ -> },
    onNavigateToAnimeDetail: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            APTitledBackTopAppBar(
                title = "평가한 작품",
                handleBackNavigation = { onNavigateBack() }
            )
        }
    ) { innerPadding ->
        val listState = rememberLazyListState()

        LaunchedEffect(listState, reviews.size) {
            snapshotFlow { listState.layoutInfo }
                .map { layoutInfo ->
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    val totalItemsCount = layoutInfo.totalItemsCount

                    lastVisibleItemIndex >= totalItemsCount - 2
                }
                .distinctUntilChanged()
                .collect { shouldLoadMore ->
                    if (shouldLoadMore && !isLoading && reviews.isNotEmpty()) {
                        onLoadMoreData(reviewResponse?.cursor?.lastId)
                    }
                }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(APColors.Surface)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "총 ${reviewResponse?.count}개",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.TextGray
                    )
                    Box {
                        Row(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { onChangeShowSortDropdown() },
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
                            onDismissRequest = { onChangeShowSortDropdown() },
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
                                        onChangeShowSortDropdown()
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
                                        onChangeShowSortDropdown()
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
                                        onChangeShowSortDropdown()
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
                                        onChangeShowSortDropdown()
                                        onChangeSort(ReviewSort.RATING_ASC)
                                    }
                                    .padding(14.dp)
                            )
                        }
                    }
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "리뷰만 보기",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        color = if (isReviewOnly) APColors.Secondary else Color(0x4D667080)
                    )
                    APToggleSwitch(
                        checked = isReviewOnly,
                        checkedColor = APColors.Secondary,
                        unCheckedColor = Color(0x4D667080),
                        onCheckedChange = { onChangeOnlyReview() },
                    )
                }
            }
            if (reviews.isEmpty() && !isLoading) {
                item {
                    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                    APEmptyContent(
                        comment = "앗! 아직 평가한 작품이 없네요.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeight - 200.dp)
                    )
                }
            }
            items(
                items = reviews.filter { it.reviewContent != null || !isReviewOnly },
                key = { review -> review.reviewId }
            ) { review ->
                APReviewItem(
                    reviewItem = review,
                    onNavigateAnimeDetail = { onNavigateToAnimeDetail(it) },
                    onClickUpdate = { onNavigateToReviewForm(review.animeId, review.reviewId, FormType.EDIT) },
                    onClickDelete = {
                        onChangeDialogData(
                            DialogData(
                                title = "정말 리뷰를 삭제하시겠어요?",
                                subTitle = "삭제한 리뷰는 복구가 불가능해요.",
                                dismiss = "취소",
                                confirm = "삭제",
                                onDismiss = { onChangeDialogData(null) },
                                onConfirm = {
                                    onDeleteReview(review.reviewId)
                                    onChangeDialogData(null)
                                },
                            )
                        )
                    },
                    isLikedLoading = isLikedLoading,
                    onClickLiked = { isLiked, callBack ->
                        onChangeLikeState(isLiked, review.reviewId) { result ->
                            callBack(result)
                        }
                    },
                )
            }
            if (isLoading) {
                item {
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

    snackBarData?.let {
        APSnackBar(
            snackBarData = it,
            visible = true,
            onDismiss = { onChangeSnackBarData(null) }
        )
    }

    dialogData?.let {
        APConfirmDialog(
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