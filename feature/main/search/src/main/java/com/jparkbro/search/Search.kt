package com.jparkbro.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.ui.APCardItem
import com.jparkbro.ui.APSearchFieldBackTopAppBar
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.util.calculateCardWidth
import com.jparkbro.ui.util.calculateItemSpacing

@Composable
internal fun Search(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateSearchResult: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val popularAnimes by viewModel.popularAnimes.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()

    val searchText by viewModel.searchText.collectAsState()

    Search(
        popularAnimes = popularAnimes,
        recentSearches = recentSearches,
        searchText = searchText,
        onChangeSearchText = viewModel::updateSearchText,
        onNavigateBack = onNavigateBack,
        onNavigateToAnimeDetail = onNavigateToAnimeDetail,
        onNavigateSearchResult = onNavigateSearchResult,
        onLoadRecentSearchKeywords = viewModel::loadRecentSearchKeywords,
        onSaveRecentSearch = viewModel::saveRecentSearch,
        onDeleteRecentSearch = viewModel::deleteRecentSearch,
        onDeleteAllRecentSearches = viewModel::deleteAllRecentSearches
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Search(
    popularAnimes: SearchUiState = SearchUiState.Loading,
    recentSearches: List<String> = emptyList(),
    searchText: String,
    onChangeSearchText: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateSearchResult: (String) -> Unit,
    onLoadRecentSearchKeywords: () -> Unit,
    onSaveRecentSearch: (String, (Boolean) -> Unit) -> Unit,
    onDeleteRecentSearch: (String, (Boolean) -> Unit) -> Unit,
    onDeleteAllRecentSearches: ((Boolean) -> Unit) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            APSearchFieldBackTopAppBar(
                value = searchText,
                onValueChange = { onChangeSearchText(it) },
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        if (searchText.isNotBlank()) {
                            onSaveRecentSearch(searchText) { onLoadRecentSearchKeywords() }
                            onNavigateSearchResult(searchText)
                        }
                    }
                ),
                handleBackNavigation = { onNavigateBack() },
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { onChangeSearchText("") }
                                .padding(4.dp)
                                .size(16.dp),
                            tint = APColors.TextGray
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_search_outline),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    focusManager.clearFocus()
                                    if (searchText.isNotBlank()) {
                                        onSaveRecentSearch(searchText) { onLoadRecentSearchKeywords() }
                                        onNavigateSearchResult(searchText)
                                    }
                                }
                                .padding(4.dp)
                                .size(16.dp),
                            tint = APColors.TextGray
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
        ) {
            if (recentSearches.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(APColors.White)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "최근 검색어",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W600,
                            color = APColors.Black
                        )
                        Text(
                            text = "전체삭제",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.Gray,
                            modifier = Modifier
                                .clickable { onDeleteAllRecentSearches { onLoadRecentSearchKeywords() } }
                        )
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(recentSearches) { search ->
                            Row(
                                modifier = Modifier
                                    .border(1.dp, APColors.LightGray, CircleShape)
                                    .clip(CircleShape)
                                    .clickable {
                                        onNavigateSearchResult(search)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 7.dp)
                                ,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = search,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W500,
                                    color = APColors.Black
                                )
                                Icon(
                                    painter = painterResource(R.drawable.ic_close),
                                    contentDescription = null,
                                    tint = APColors.Gray,
                                    modifier = Modifier
                                        .size(15.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            onDeleteRecentSearch(search) { onLoadRecentSearchKeywords() }
                                        }
                                )
                            }
                        }
                    }
                }
                Spacer(
                    modifier = Modifier
                        .background(APColors.Surface)
                        .fillMaxWidth()
                        .height(8.dp)
                )
            }
            when (popularAnimes) {
                is SearchUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is SearchUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(APColors.White)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(17.dp)
                    ) {
                        Text(
                            text = "인기 작품",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W600,
                            color = APColors.Black
                        )
                        val cardWidth = calculateCardWidth(maxWidth = 115.dp)
                        val spacing = calculateItemSpacing(itemWidth = cardWidth)
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(spacing),
                            verticalArrangement = Arrangement.spacedBy(27.dp),
                            maxItemsInEachRow = 3
                        ) {
                            popularAnimes.data.forEach { anime ->
                                APCardItem(
                                    title = "${anime.title}",
                                    imageUrl = anime.coverImageUrl,
                                    cardWidth = cardWidth,
                                    cardHeight = cardWidth * 1.41f,
                                    fontSize = 14.sp,
                                    maxLine = 1,
                                    onClick = { onNavigateToAnimeDetail(anime.animeId) }
                                )
                            }
                        }
                    }
                }
                is SearchUiState.Error -> {} // TODO
            }
        }
    }
}