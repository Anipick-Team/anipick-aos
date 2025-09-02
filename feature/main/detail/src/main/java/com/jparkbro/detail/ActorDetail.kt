package com.jparkbro.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.jparkbro.model.detail.ActorDetailResponse
import com.jparkbro.model.detail.ActorFilmography
import com.jparkbro.ui.APCardItem
import com.jparkbro.ui.APTitledBackTopAppBar
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun ActorDetail(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    viewModel: ActorDetailViewModel = hiltViewModel()
) {

    val response by viewModel.response.collectAsState()
    val workList by viewModel.workList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val isLikeLoading by viewModel.isLikeLoading.collectAsState()

    ActorDetail(
        response = response,
        workList = workList,
        isLoading = isLoading,
        isLikeLoading = isLikeLoading,
        onLoadMoreData = viewModel::loadData,
        onChangeLiked = viewModel::likePerson,
        onNavigateBack = onNavigateBack,
        onNavigateToAnimeDetail = onNavigateToAnimeDetail,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActorDetail(
    response: ActorDetailResponse? = null,
    workList: List<ActorFilmography> = emptyList(),
    isLoading: Boolean = false,
    isLikeLoading: Boolean = false,
    onLoadMoreData: () -> Unit,
    onChangeLiked: (Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
) {
    val listState = rememberLazyGridState()
    LaunchedEffect(listState, workList.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 2
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !isLoading && workList.isNotEmpty()) {
                    val lastId = response?.cursor?.lastId
                    if (lastId != null) {
                        onLoadMoreData()
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            APTitledBackTopAppBar(
                title = "성우",
                handleBackNavigation = { onNavigateBack() },
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 12.dp)
                .background(APColors.White),
            contentPadding = PaddingValues(vertical = 20.dp),
            state = listState,
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item(span = { GridItemSpan(3) }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = response?.profileImageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(93.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(APColors.LightGray, RoundedCornerShape(8.dp)),
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${response?.name}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W600,
                                color = APColors.Black
                            )
                            Icon(
                                painter = painterResource(if (response?.isLiked == true) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(19.dp)
                                    .clickable(
                                        enabled = !isLikeLoading
                                    ) { onChangeLiked(response?.isLiked == true) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 5.dp,
                        color = APColors.Surface
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "참여 작품 목록",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.Black,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "총 ${response?.count}개",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.TextGray,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                }
            }
            items(workList) { work ->
                APCardItem(
                    title = "${work.characterName}",
                    imageUrl = "${work.characterImageUrl}",
                    cardWidth = 115.dp,
                    cardHeight = 105.dp,
                    fontSize = 14.sp,
                    description = {
                        Text(
                            text = "${work.animeTitle}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.TextGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    maxLine = 1,
                    onClick = { onNavigateToAnimeDetail(work.animeId) }
                )
            }
        }
    }
}