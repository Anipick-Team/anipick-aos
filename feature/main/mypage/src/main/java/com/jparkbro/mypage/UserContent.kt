package com.jparkbro.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.model.common.FormType
import com.jparkbro.model.home.ComingSoonItem
import com.jparkbro.model.mypage.ContentType
import com.jparkbro.model.mypage.LikedAnime
import com.jparkbro.model.mypage.LikedPerson
import com.jparkbro.model.mypage.UserContentAnime
import com.jparkbro.model.mypage.UserContentResponse
import com.jparkbro.ui.APCardItem
import com.jparkbro.ui.APTitledBackTopAppBar
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.util.calculateItemSpacing
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun UserContent(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onCheckStatusRefresh: () -> Boolean,
    onStatusRefresh: () -> Unit,
    viewModel: UserContentViewModel = hiltViewModel()
) {
    val type = viewModel.type

    val uiState by viewModel.uiState.collectAsState()
    val responseData by viewModel.responseData.collectAsState()
    val dataList by viewModel.dataList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    UserContent(
        type = type,
        uiState = uiState,
        responseData = responseData,
        dataList = dataList,
        isLoading = isLoading,
        onLoadMoreData = viewModel::loadData,
        onRefreshData = viewModel::refreshData,
        onNavigateBack = onNavigateBack,
        onNavigateToAnimeDetail = onNavigateToAnimeDetail,
        onCheckStatusRefresh = onCheckStatusRefresh,
        onStatusRefresh = onStatusRefresh,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserContent(
    type: ContentType = ContentType.WATCHLIST,
    uiState: UserContentUiState = UserContentUiState.Loading,
    responseData: UserContentResponse? = null,
    dataList: List<Any> = emptyList(),
    isLoading: Boolean = false,
    onLoadMoreData: (Int?) -> Unit = {},
    onRefreshData: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigateToAnimeDetail: (Int) -> Unit = {},
    onCheckStatusRefresh: () -> Boolean = { false },
    onStatusRefresh: () -> Unit,
) {
    LaunchedEffect(Unit) {
        val isStatusUpdate = onCheckStatusRefresh()
        if (isStatusUpdate) {
            onRefreshData()
            onStatusRefresh()
        }
    }

    Scaffold(
        topBar = {
            APTitledBackTopAppBar(
                title = type.title,
                handleBackNavigation = { onNavigateBack() },
            )
        },
    ) { innerPadding ->
        val gridState = rememberLazyGridState()

        LaunchedEffect(gridState, dataList.size) {
            snapshotFlow { gridState.layoutInfo }
                .map { layoutInfo ->
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    val totalItemsCount = layoutInfo.totalItemsCount

                    lastVisibleItemIndex >= totalItemsCount - 6
                }
                .distinctUntilChanged()
                .collect { shouldLoadMore ->
                    if (shouldLoadMore && !isLoading && dataList.isNotEmpty()) {
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
                .padding(innerPadding)
                .background(APColors.White),
            contentPadding = PaddingValues(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(27.dp),
            horizontalArrangement = Arrangement.spacedBy(calculateItemSpacing()),
        ) {
            item(span = { GridItemSpan(3) }) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally, true)
                        .width(LocalConfiguration.current.screenWidthDp.dp)
                        .height(12.dp)
                        .background(APColors.Surface)
                )
            }
            item(span = { GridItemSpan(3) }) {
                val countText = when (type) {
                    ContentType.LIKED_PERSON -> "총 ${responseData?.count}명"
                    else -> "총 ${responseData?.count}개"
                }
                Text(
                    text = countText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.TextGray,
                    modifier = Modifier
                )
            }
            items(
                count = dataList.size,
                key = { index ->
                    val item = dataList[index]
                    when (item) {
                        is LikedPerson -> item.personId
                        is UserContentAnime -> item.animeId
                        else -> item.hashCode()
                    }
                }
            ) { index ->
                val item = dataList[index]
                when (type) {
                    ContentType.LIKED_PERSON -> {
                        val person = item as LikedPerson
                        APCardItem(
                            title = "${person.name}",
                            imageUrl = person.imageUrl,
                            cardWidth = 115.dp,
                            cardHeight = 105.dp,
                            fontSize = 14.sp,
                            maxLine = 1,
                            onClick = {}
                        )
                    }
                    else -> {
                        val anime = item as UserContentAnime
                        APCardItem(
                            title = "${anime.title}",
                            imageUrl = anime.imageUrl,
                            rating = anime.myRating?.let { "$it" },
                            cardWidth = 115.dp,
                            cardHeight = 162.dp,
                            fontSize = 14.sp,
                            maxLine = 2,
                            onClick = { onNavigateToAnimeDetail(anime.animeId) },
                            description = {
                                if (type == ContentType.FINISHED && anime.myRating == null) {
                                    Text(
                                        text = "아직 평가가 없어요",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W400,
                                        color = APColors.TextGray
                                    )
                                }
                            }
                        )
                    }
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
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun UserContentPreview() {
    UserContent(
        onStatusRefresh = {}
    )
}