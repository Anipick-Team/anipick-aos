package com.jparkbro.search

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.model.search.SearchResultAnime
import com.jparkbro.model.search.SearchResultPerson
import com.jparkbro.model.search.SearchResultResponse
import com.jparkbro.model.search.SearchResultStudio
import com.jparkbro.model.search.SearchType
import com.jparkbro.ui.APCardItem
import com.jparkbro.ui.APSearchFieldBackTopAppBar
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.util.calculateItemSpacing
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun SearchResult(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    viewModel: SearchResultViewModel = hiltViewModel()
) {
    val searchText by viewModel.searchText.collectAsState()
    val searchType by viewModel.searchType.collectAsState()

    val uiState by viewModel.uiState.collectAsState()
    val response by viewModel.response.collectAsState()
    val currentDataList by viewModel.currentDataList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    SearchResult(
        searchText = searchText,
        onChangeSearchText = viewModel::updateSearchText,
        searchType = searchType,
        onChangeSearchType = viewModel::updateSearchType,
        uiState = uiState,
        response = response,
        currentDataList = currentDataList,
        isLoading = isLoading,
        onSearchResult = viewModel::loadSearchResults,
        onSaveRecentSearch = viewModel::saveRecentSearch,
        onSubmitAnimeLog = viewModel::submitAnimeLog,
        onNavigateBack = onNavigateBack,
        onNavigateToAnimeDetail = onNavigateToAnimeDetail,
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResult(
    searchText: String,
    onChangeSearchText: (String) -> Unit,
    searchType: SearchType,
    onChangeSearchType: (SearchType) -> Unit,
    uiState: SearchResultUiState = SearchResultUiState.Loading,
    response: SearchResultResponse? = null,
    currentDataList: List<Any> = emptyList(),
    isLoading: Boolean,
    onSearchResult: (Int?, Int?) -> Unit,
    onSaveRecentSearch: (String) -> Unit,
    onSubmitAnimeLog: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
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
                        onSearchResult(null, null)
                        onSaveRecentSearch(searchText)
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
                                    onSearchResult(null, null)
                                    onSaveRecentSearch(searchText)
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
        when (uiState) {
            is SearchResultUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(APColors.White),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is SearchResultUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(APColors.White)
                        .padding(innerPadding)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { focusManager.clearFocus() }
                ) {
                    SearchTabs(
                        result = response,
                        searchType = searchType,
                        onChangeSearchType = { onChangeSearchType(it) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 2.dp,
                        color = APColors.LightGray
                    )

                    val gridState = rememberLazyGridState()
                    LaunchedEffect(gridState, currentDataList.size) {
                        snapshotFlow { gridState.layoutInfo }
                            .map { layoutInfo ->
                                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                                val totalItemsCount = layoutInfo.totalItemsCount

                                lastVisibleItemIndex >= totalItemsCount - 6
                            }
                            .distinctUntilChanged()
                            .collect { shouldLoadMore ->
                                if (shouldLoadMore && !isLoading && currentDataList.isNotEmpty()) {
                                    onSearchResult(response?.cursor?.lastId, response?.nextPage)
                                }
                            }
                    }
                    val cellCount = if (searchType == SearchType.STUDIOS) 1 else 3
                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(cellCount),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(20.dp)
                    ) {
                        item(span = { GridItemSpan(cellCount) }) {
                            val countText = when (searchType) {
                                SearchType.ANIMES -> "총 ${response?.count}개"
                                SearchType.PERSONS -> "총 ${response?.count}명"
                                SearchType.STUDIOS -> "총 ${response?.count}개"
                            }
                            Text(
                                text = countText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.TextGray
                            )
                        }
                        items(currentDataList) { item ->
                            when (searchType) {
                                SearchType.ANIMES -> {
                                    val anime = item as SearchResultAnime
                                    var hasLogged by rememberSaveable(anime.animeId) { mutableStateOf(false) }
                                    LaunchedEffect(anime.animeId) {
                                        if (!hasLogged) {
                                            onSubmitAnimeLog(anime.impressionLog)
                                            hasLogged = true
                                        }
                                    }
                                    APCardItem(
                                        title = "${anime.title}",
                                        imageUrl = anime.coverImageUrl,
                                        cardWidth = 115.dp,
                                        cardHeight = 162.dp,
                                        fontSize = 14.sp,
                                        maxLine = 1,
                                        onClick = {
                                            onSubmitAnimeLog(anime.clickLog)
                                            onNavigateToAnimeDetail(anime.animeId)
                                        }
                                    )
                                }
                                SearchType.PERSONS -> {
                                    val person = item as SearchResultPerson
                                    APCardItem(
                                        title = "${person.name}",
                                        imageUrl = person.profileImage,
                                        cardWidth = 115.dp,
                                        cardHeight = 105.dp,
                                        fontSize = 14.sp,
                                        maxLine = 1,
                                        onClick = { onNavigateToAnimeDetail(person.personId) }
                                    )
                                }
                                SearchType.STUDIOS -> {
                                    val studio = item as SearchResultStudio
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                            .clickable {},
                                        horizontalArrangement = Arrangement.Absolute.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${studio.name}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.W500,
                                            color = APColors.Black
                                        )
                                        Icon(
                                            painter = painterResource(R.drawable.ic_chevron_right),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(18.dp),
                                            tint = Color(0xFFC3C3CA)
                                        )
                                    }
                                }
                            }
                        }
                        if (isLoading) {
                            item(span = { GridItemSpan(cellCount) }) {
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
            is SearchResultUiState.Error -> {}
        }
    }
}

@Composable
private fun SearchTabs(
    result: SearchResultResponse?,
    searchType: SearchType,
    onChangeSearchType: (SearchType) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SearchType.entries.forEach { type ->
                val isSelected = searchType == type
                val count = when (type) {
                    SearchType.ANIMES -> if (isSelected) result?.count else result?.animeCount
                    SearchType.PERSONS -> if (isSelected) result?.count else result?.personCount
                    SearchType.STUDIOS -> if (isSelected) result?.count else result?.studioCount
                }

                SearchTab(
                    count = count,
                    type = type,
                    onChangeSearchType = { onChangeSearchType(it) },
                    isSelected = isSelected,
                )
            }
        }
    }
}

@Composable
private fun SearchTab(
    count: Int?,
    type: SearchType,
    onChangeSearchType: (SearchType) -> Unit,
    isSelected: Boolean,
) {
    val indicatorWidth = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onChangeSearchType(type) }
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(vertical = 16.dp)
                .onGloballyPositioned { coordinates ->
                if (isSelected) {
                    indicatorWidth.value = coordinates.size.width
                }
            },
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = type.displayName,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = if (isSelected) APColors.Black else APColors.TextGray
            )
            Text(
                text = "${count}건",
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = APColors.Gray
            )
        }
        Spacer(
            modifier = Modifier
                .width(with(LocalDensity.current) { indicatorWidth.value.toDp() })
                .height(2.dp)
                .background(APColors.Black)
        )
    }
}