package com.jparkbro.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.detail.AnimeRecommendsResponse
import com.jparkbro.ui.APCardItem
import com.jparkbro.ui.APTitledBackTopAppBar
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.util.calculateCardWidth
import com.jparkbro.ui.util.calculateItemSpacing
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun AnimeRecommends(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    viewModel: AnimeRecommendsViewModel = hiltViewModel()
) {
    val response by viewModel.response.collectAsState()
    val animeList by viewModel.animeList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    AnimeRecommends(
        response = response,
        animeList = animeList,
        isLoading = isLoading,
        onLoadMoreData = viewModel::loadData,
        onNavigateBack = onNavigateBack,
        onNavigateToAnimeDetail = onNavigateToAnimeDetail,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeRecommends(
    response: AnimeRecommendsResponse? = null,
    animeList: List<DefaultAnime> = emptyList(),
    isLoading: Boolean = false,
    onLoadMoreData: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            APTitledBackTopAppBar(
                title = "함께 볼만한 작품",
                handleBackNavigation = { onNavigateBack() },
            )
        },
        containerColor = APColors.Surface
    ) { innerPadding ->
        val gridState = rememberLazyGridState()

        LaunchedEffect(gridState, animeList.size) {
            snapshotFlow { gridState.layoutInfo }
                .map { layoutInfo ->
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    val totalItemsCount = layoutInfo.totalItemsCount

                    lastVisibleItemIndex >= totalItemsCount - 3
                }
                .distinctUntilChanged()
                .collect { shouldLoadMore ->
                    if (shouldLoadMore && !isLoading && animeList.isNotEmpty()) {
                        val lastId = response?.cursor?.lastId
                        if (lastId != null) {
                            onLoadMoreData()
                        }
                    }
                }
        }

        val cardWidth = calculateCardWidth(maxWidth = 115.dp)
        val spacing = calculateItemSpacing(itemWidth = cardWidth)
        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing),
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
                        Text(
                            text = "'${response?.referenceAnimeTitle}' 와",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W600,
                            color = APColors.White,
                            lineHeight = (1.4).em,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = "함께 보기 좋은 작품",
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
            items(animeList) { anime ->
                APCardItem(
                    title = "${anime.title}",
                    imageUrl = anime.coverImageUrl,
                    cardWidth = cardWidth,
                    cardHeight = cardWidth * 1.41f,
                    fontSize = 14.sp,
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