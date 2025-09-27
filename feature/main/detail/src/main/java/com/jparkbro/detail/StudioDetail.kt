package com.jparkbro.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.model.detail.DetailStudio
import com.jparkbro.model.detail.StudioAnime
import com.jparkbro.ui.APCardItem
import com.jparkbro.ui.APTitledBackTopAppBar
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.util.calculateItemSpacing
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun StudioDetail(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    viewModel: StudioDetailViewModel = hiltViewModel()
) {
    val studioInfo by viewModel.studioInfo.collectAsState()
    val dataList by viewModel.dataList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    StudioDetail(
        studioInfo = studioInfo,
        dataList = dataList,
        isLoading = isLoading,
        onLoadMoreData = viewModel::loadData,
        onNavigateBack = onNavigateBack,
        onNavigateToAnimeDetail = onNavigateToAnimeDetail,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudioDetail(
    studioInfo: DetailStudio? = null,
    dataList: List<StudioAnime> = emptyList(),
    isLoading: Boolean = false,
    onLoadMoreData: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigateToAnimeDetail: (Int) -> Unit,
) {
    val listState = rememberLazyListState()
    LaunchedEffect(listState, dataList.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 2
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !isLoading && dataList.isNotEmpty()) {
                    val lastId = studioInfo?.cursor?.lastId
                    if (lastId != null) {
                        onLoadMoreData()
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            APTitledBackTopAppBar(
                title = "${studioInfo?.studioName}",
                handleBackNavigation = { onNavigateBack() },
            )
        },
        containerColor = APColors.Surface,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            if (studioInfo == null) {

            } else {
                if (dataList.isEmpty()) {
                    // TODO
                } else {
                    animeByYearSection(
                        dataList = dataList,
                        onNavigateToAnimeDetail = onNavigateToAnimeDetail,
                    )
                }
            }
        }
    }
}

private fun LazyListScope.animeByYearSection(
    dataList: List<StudioAnime> = emptyList(),
    onNavigateToAnimeDetail: (Int) -> Unit,
) {

    val groupByYear = dataList
        .groupBy { it.seasonYear }
        .toSortedMap(reverseOrder())

    groupByYear.forEach { (year, animeList) ->
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(APColors.White)
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = year,
                    fontWeight = FontWeight.W500,
                    fontSize = 14.sp,
                    color = APColors.Black,
                    modifier = Modifier
                        .background(color = APColors.LightGray, CircleShape)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(calculateItemSpacing()),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 3
                ) {
                    animeList.forEach { anime ->
                        APCardItem(
                            title = "${anime.title}",
                            imageUrl = "${anime.coverImageUrl}",
                            cardWidth = 115.dp,
                            cardHeight = 162.dp,
                            fontSize = 14.sp,
                            maxLine = 2,
                            onClick = { onNavigateToAnimeDetail(anime.animeId) }
                        )
                    }
                }
            }
        }
    }
}