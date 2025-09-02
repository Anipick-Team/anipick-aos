package com.jparkbro.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.model.common.FormType
import com.jparkbro.model.home.ComingSoonItem
import com.jparkbro.model.home.ContentType
import com.jparkbro.model.home.HomeDetailResponse
import com.jparkbro.model.home.HomeReviewItem
import com.jparkbro.model.home.Sort
import com.jparkbro.ui.APCardItem
import com.jparkbro.ui.APDialog
import com.jparkbro.ui.APReviewItem
import com.jparkbro.ui.APTitledBackTopAppBar
import com.jparkbro.ui.APToggleSwitch
import com.jparkbro.ui.DialogData
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.util.calculateItemSpacing
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun HomeDetail(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateToReviewForm: (Int, Int, FormType) -> Unit,
    viewModel: HomeDetailViewModel = hiltViewModel()
) {
    val type = viewModel.type
    val nickname by viewModel.nickname.collectAsState()

    val sort by viewModel.sort.collectAsState()
    val showSortDropdown by viewModel.showSortDropdown.collectAsState()
    val includeAdult by viewModel.includeAdult.collectAsState()
    val dialogData by viewModel.dialogData.collectAsState()

    val uiState by viewModel.uiState.collectAsState()
    val responseData by viewModel.responseData.collectAsState()
    val items by viewModel.items.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val isLikedLoading by viewModel.isLikedLoading.collectAsState()

    HomeDetail(
        type = type,
        nickname = nickname,
        sort = sort,
        showSortDropdown = showSortDropdown,
        includeAdult = includeAdult,
        dialogData = dialogData,
        onChangeDialogData = viewModel::updateDialogData,
        uiState = uiState,
        responseData = responseData,
        items = items,
        isLoading = isLoading,
        isLikedLoading = isLikedLoading,
        onChangeIncludeAdult = viewModel::toggleIncludeAdult,
        onChangeSort = viewModel::updateSort,
        onChangeSortDropdown = viewModel::changeDropdownState,
        onLoadMoreData = viewModel::loadData,
        onChangeLikeState = viewModel::updateLikeState,
        onDeleteReview = viewModel::deleteReview,
        onReportReview = viewModel::reportReview,
        onBlockUser = viewModel::blockUser,
        onNavigateBack = onNavigateBack,
        onNavigateToAnimeDetail = onNavigateToAnimeDetail,
        onNavigateToReviewForm = onNavigateToReviewForm,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeDetail(
    type: ContentType,
    nickname: String,
    sort: Sort = Sort.LATEST,
    showSortDropdown: Boolean = false,
    includeAdult: Boolean = false,
    dialogData: DialogData? = null,
    onChangeDialogData: (DialogData?) -> Unit,
    uiState: HomeDetailUiState = HomeDetailUiState.Loading,
    responseData: HomeDetailResponse? = null,
    items: List<Any> = emptyList(),
    isLoading: Boolean = false,
    isLikedLoading: Boolean = false,
    onChangeIncludeAdult: () -> Unit,
    onChangeSort: (Sort) -> Unit,
    onChangeSortDropdown: () -> Unit,
    onLoadMoreData: (Int?) -> Unit,
    onChangeLikeState: (Boolean, Int, (Boolean) -> Unit) -> Unit,
    onDeleteReview: (Int) -> Unit,
    onReportReview: (Int, String) -> Unit,
    onBlockUser: (Int) -> Unit,
    onNavigateBack: () -> Unit = {},
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateToReviewForm: (Int, Int, FormType) -> Unit,
) {
    Scaffold(
        topBar = {
            APTitledBackTopAppBar(
                title = type.title,
                handleBackNavigation = { onNavigateBack() },
            )
        },
    ) { innerPadding ->
        when (type) {
            ContentType.RECENT_REVIEW -> {
                val listState = rememberLazyListState()

                LaunchedEffect(listState, items.size) {
                    snapshotFlow { listState.layoutInfo }
                        .map { layoutInfo ->
                            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                            val totalItemsCount = layoutInfo.totalItemsCount

                            lastVisibleItemIndex >= totalItemsCount - 2
                        }
                        .distinctUntilChanged()
                        .collect { shouldLoadMore ->
                            if (shouldLoadMore && !isLoading && items.isNotEmpty()) {
                                val lastId = responseData?.cursor?.lastId
                                if (lastId != null) {
                                    onLoadMoreData(lastId)
                                }
                            }
                        }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(APColors.Surface)
                        .padding(innerPadding),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    state = listState
                ) {
                    items(
                        items = items,
                        key = { review ->
                            when (review) {
                                is HomeReviewItem -> review.reviewId
                                else -> review.hashCode()
                            }
                        }
                    ) { review ->
                        val radioOptions = listOf("스포일러", "편파적인 언행", "욕설 및 비하", "홍보성 및 영리 목적", "음란성 및 선정성")
                        var selectedOption by remember { mutableStateOf(radioOptions[0]) }
                        APReviewItem(
                            reviewItem = review as HomeReviewItem,
                            onNavigateAnimeDetail = { onNavigateToAnimeDetail(it) },
                            onClickUpdate = { onNavigateToReviewForm(review.animeId, review.reviewId, FormType.EDIT) },
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
            ContentType.RECOMMEND -> {
                val gridState = rememberLazyGridState()

                LaunchedEffect(gridState, items.size) {
                    snapshotFlow { gridState.layoutInfo }
                        .map { layoutInfo ->
                            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                            val totalItemsCount = layoutInfo.totalItemsCount

                            lastVisibleItemIndex >= totalItemsCount - 6
                        }
                        .distinctUntilChanged()
                        .collect { shouldLoadMore ->
                            if (shouldLoadMore && !isLoading && items.isNotEmpty()) {
                                val lastId = responseData?.cursor?.lastId
                                if (lastId != null) {
                                    onLoadMoreData(lastId)
                                }
                            }
                        }
                }

                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(calculateItemSpacing()),
                ) {
                    item(span = { GridItemSpan(3) }) {
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .background(APColors.Surface))
                    }
                    item(span = { GridItemSpan(3) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF010000), RoundedCornerShape(8.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(20.dp)
                            ) {
                                if (responseData?.referenceAnimeTitle == null) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "오늘의 추천작, ",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.W600,
                                            color = APColors.White,
                                            lineHeight = (1.4).em
                                        )
                                        Text(
                                            text = nickname,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.W600,
                                            color = APColors.White,
                                            lineHeight = (1.4).em,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )
                                        Text(
                                            text = " 님의",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.W600,
                                            color = APColors.White,
                                            lineHeight = (1.4).em
                                        )
                                    }
                                    Text(
                                        text = "취향에 맞춰 준비했어요!",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.W600,
                                        color = APColors.White,
                                        lineHeight = (1.4).em
                                    )
                                } else {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${responseData.referenceAnimeTitle}",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.W600,
                                            color = APColors.White,
                                            lineHeight = (1.4).em,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )
                                        Text(
                                            text = " 를 재밌게 보셨다면,",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.W600,
                                            color = APColors.White,
                                            lineHeight = (1.4).em
                                        )
                                    }
                                    Text(
                                        text = "이 작품들도 마음에 드실 거에요!",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.W600,
                                        color = APColors.White,
                                        lineHeight = (1.4).em
                                    )
                                }
                            }
                            Image(
                                painter = painterResource(R.drawable.home_banner_mascot),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(top = 76.dp, end = 24.dp)
                            )
                        }
                    }
                    items(
                        count = items.size,
                        key = { index ->
                            val item = items[index]
                            when (item) {
                                is ComingSoonItem -> item.animeId
                                else -> item.hashCode()
                            }
                        }
                    ) { index ->
                        val item = items[index]
                        if (item is ComingSoonItem) {
                            APCardItem(
                                title = "${item.title}",
                                imageUrl = item.coverImageUrl,
                                cardWidth = 115.dp,
                                cardHeight = 162.dp,
                                fontSize = 14.sp,
                                maxLine = 1,
                                onClick = { onNavigateToAnimeDetail(item.animeId) }
                            )
                        }
                    }
                    if (isLoading) {
                        item(span = { GridItemSpan(3) }) {
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
            ContentType.RECENT_RECOMMEND -> {
                val gridState = rememberLazyGridState()

                LaunchedEffect(gridState, items.size) {
                    snapshotFlow { gridState.layoutInfo }
                        .map { layoutInfo ->
                            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                            val totalItemsCount = layoutInfo.totalItemsCount

                            lastVisibleItemIndex >= totalItemsCount - 6
                        }
                        .distinctUntilChanged()
                        .collect { shouldLoadMore ->
                            if (shouldLoadMore && !isLoading && items.isNotEmpty()) {
                                val lastId = responseData?.cursor?.lastId
                                if (lastId != null) {
                                    onLoadMoreData(lastId)
                                }
                            }
                        }
                }

                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(calculateItemSpacing()),
                ) {
                    item(span = { GridItemSpan(3) }) {
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .background(APColors.Surface))
                    }
                    item(span = { GridItemSpan(3) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF010000), RoundedCornerShape(8.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "최근 찾아보신 ",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.W600,
                                        color = APColors.White,
                                        lineHeight = (1.4).em
                                    )
                                    Text(
                                        text = "${responseData?.referenceAnimeTitle}",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.W600,
                                        color = APColors.White,
                                        lineHeight = (1.4).em,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    Text(
                                        text = " 과",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.W600,
                                        color = APColors.White,
                                        lineHeight = (1.4).em
                                    )
                                }
                                Text(
                                    text = "비슷한 작품이에요!",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.W600,
                                    color = APColors.White,
                                    lineHeight = (1.4).em
                                )
                            }
                            Image(
                                painter = painterResource(R.drawable.home_banner_mascot),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(top = 76.dp, end = 24.dp)
                            )
                        }
                    }
                    items(
                        count = items.size,
                        key = { index ->
                            val item = items[index]
                            when (item) {
                                is ComingSoonItem -> item.animeId
                                else -> item.hashCode()
                            }
                        }
                    ) { index ->
                        val item = items[index]
                        if (item is ComingSoonItem) {
                            APCardItem(
                                title = "${item.title}",
                                imageUrl = item.coverImageUrl,
                                cardWidth = 115.dp,
                                cardHeight = 162.dp,
                                fontSize = 14.sp,
                                maxLine = 1,
                                onClick = { onNavigateToAnimeDetail(item.animeId) }
                            )
                        }
                    }
                    if (isLoading) {
                        item(span = { GridItemSpan(3) }) {
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
            ContentType.COMING_SOON -> {
                val gridState = rememberLazyGridState()

                LaunchedEffect(gridState, items.size) {
                    snapshotFlow { gridState.layoutInfo }
                        .map { layoutInfo ->
                            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                            val totalItemsCount = layoutInfo.totalItemsCount

                            lastVisibleItemIndex >= totalItemsCount - 6
                        }
                        .distinctUntilChanged()
                        .collect { shouldLoadMore ->
                            if (shouldLoadMore && !isLoading && items.isNotEmpty()) {
                                val lastId = responseData?.cursor?.lastId
                                if (lastId != null) {
                                    onLoadMoreData(lastId)
                                }
                            }
                        }
                }
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(calculateItemSpacing()),
                ) {
                    item(span = { GridItemSpan(3) }) {
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .background(APColors.Surface))
                    }
                    item(span = { GridItemSpan(3) }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "19세",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W500,
                                    color = APColors.Point
                                )
                                APToggleSwitch(
                                    checked = includeAdult,
                                    checkedColor = APColors.Point,
                                    unCheckedColor = Color(0x4D667080),
                                    onCheckedChange = { onChangeIncludeAdult() }
                                )
                            }
                            Box {
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
                                        text = Sort.LATEST.displayName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W500,
                                        color = if (sort == Sort.LATEST) APColors.Black else APColors.TextGray,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                onChangeSortDropdown()
                                                onChangeSort(Sort.LATEST)
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
                                        text = Sort.POPULARITY.displayName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W500,
                                        color = if (sort == Sort.POPULARITY) APColors.Black else APColors.TextGray,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                onChangeSortDropdown()
                                                onChangeSort(Sort.POPULARITY)
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
                                        text = Sort.START_DATE.displayName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W500,
                                        color = if (sort == Sort.START_DATE) APColors.Black else APColors.TextGray,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                onChangeSortDropdown()
                                                onChangeSort(Sort.START_DATE)
                                            }
                                            .padding(14.dp)
                                    )
                                }
                            }
                        }
                    }
                    items(
                        items = items.filterIsInstance<ComingSoonItem>().filter { !(it.isAdult ?: false) || includeAdult },
                        key = { item -> item.animeId }
                    ) { anime ->
                        APCardItem(
                            title = "${anime.title}",
                            imageUrl = anime.coverImageUrl,
                            cardWidth = 115.dp,
                            cardHeight = 162.dp,
                            fontSize = 14.sp,
                            maxLine = 1,
                            onClick = { onNavigateToAnimeDetail(anime.animeId) }
                        )
                    }
                    if (isLoading) {
                        item(span = { GridItemSpan(3) }) {
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
        }
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