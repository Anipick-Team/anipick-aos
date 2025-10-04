package com.jparkbro.explore

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.common.MetaData
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.explore.ExploreResponse
import com.jparkbro.model.home.ComingSoonItem
import com.jparkbro.ui.APCardItem
import com.jparkbro.ui.APEmptyContent
import com.jparkbro.ui.APExpireBottomSheet
import com.jparkbro.ui.APFilterTriggerChip
import com.jparkbro.ui.APLogoSearchTopAppBar
import com.jparkbro.ui.FilterParams
import com.jparkbro.ui.FilterType
import com.jparkbro.ui.R
import com.jparkbro.ui.SheetData
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.util.calculateCardWidth
import com.jparkbro.ui.util.calculateItemSpacing
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun Explore(
    metaData: MetaData,
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val exploreResponse by viewModel.exploreResponse.collectAsState()
    val animes by viewModel.animes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val bottomSheetData by viewModel.bottomSheetData.collectAsState()

    val yearFilter by viewModel.yearFilter.collectAsState()
    val quarterFilter by viewModel.quarterFilter.collectAsState()
    val genreFilter by viewModel.genreFilter.collectAsState()
    val typeFilter by viewModel.typeFilter.collectAsState()
    val isMatchAllConditions by viewModel.isMatchAllConditions.collectAsState()

    val sortType by viewModel.sortType.collectAsState()
    val showSortDropdown by viewModel.showSortDropdown.collectAsState()

    Explore(
        uiState = uiState,
        exploreResponse = exploreResponse,
        animes = animes,
        isLoading = isLoading,
        metaData = metaData,
        bottomNav = bottomNav,
        yearFilter = yearFilter,
        quarterFilter = quarterFilter,
        genreFilter = genreFilter,
        typeFilter = typeFilter,
        isMatchAllConditions = isMatchAllConditions,
        bottomSheetData = bottomSheetData,
        onBottomSheetDataChange = viewModel::updateBottomSheetData,
        onApply = viewModel::updateFilters,
        onCancelFilter = viewModel::cancelFilter,
        sortType = sortType,
        showSortDropdown = showSortDropdown,
        onChangeSortType = viewModel::updateSortType,
        onChangeSortDropdown = viewModel::changeDropdownState,
        onLoadMoreAnimes = viewModel::loadAnimes,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToAnimeDetail = onNavigateToAnimeDetail,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Explore(
    uiState: ExploreUiState = ExploreUiState.Loading,
    exploreResponse: ExploreResponse? = null,
    animes: List<DefaultAnime> = emptyList(),
    isLoading: Boolean = false,
    metaData: MetaData = MetaData(),
    bottomNav: @Composable () -> Unit,
    yearFilter: String = "",
    quarterFilter: String = "",
    genreFilter: List<ResponseMap> = emptyList(),
    typeFilter: String = "",
    bottomSheetData: SheetData? = null,
    isMatchAllConditions: Boolean = false,
    onBottomSheetDataChange: (SheetData?) -> Unit = { null },
    onApply: (params: FilterParams) -> Unit = {},
    onCancelFilter: (type: CancelFilterType, item: Int?) -> Unit,
    sortType: String = "인기순",
    showSortDropdown: Boolean = false,
    onChangeSortType: (String) -> Unit,
    onChangeSortDropdown: () -> Unit,
    onLoadMoreAnimes: (Int?) -> Unit,
    onNavigateToSearch: () -> Unit = {},
    onNavigateToAnimeDetail: (Int) -> Unit = {},
) {
    Scaffold(
        topBar = {
            APLogoSearchTopAppBar(
                onNavigateToSearch = { onNavigateToSearch() }
            )
        },
        bottomBar = { bottomNav() },
        containerColor = APColors.Surface,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                APFilterTriggerChip(
                    title = "년도/분기",
                    isSelected = yearFilter != "전체년도",
                    onClick = {
                        onBottomSheetDataChange(
                            SheetData(
                                type = FilterType.YEAR_AND_QUARTER,
                                initData = FilterParams(
                                    year = yearFilter,
                                    quarter = quarterFilter,
                                    genres = genreFilter,
                                    type = typeFilter,
                                    isMatchAllConditions = isMatchAllConditions
                                ),
                                onDismiss = { onBottomSheetDataChange(null) },
                                onConfirm = { onBottomSheetDataChange(null) },
                            )
                        )
                    }
                )
                APFilterTriggerChip(
                    title = "장르",
                    isSelected = !genreFilter.isEmpty(),
                    onClick = {
                        onBottomSheetDataChange(
                            SheetData(
                                type = FilterType.GENRE,
                                initData = FilterParams(
                                    year = yearFilter,
                                    quarter = quarterFilter,
                                    genres = genreFilter,
                                    type = typeFilter,
                                    isMatchAllConditions = isMatchAllConditions
                                ),
                                onDismiss = { onBottomSheetDataChange(null) },
                                onConfirm = { onBottomSheetDataChange(null) },
                            )
                        )
                    }
                )
                APFilterTriggerChip(
                    title = "타입",
                    isSelected = !typeFilter.isEmpty(),
                    onClick = {
                        onBottomSheetDataChange(
                            SheetData(
                                type = FilterType.TYPE,
                                initData = FilterParams(
                                    year = yearFilter,
                                    quarter = quarterFilter,
                                    genres = genreFilter,
                                    type = typeFilter,
                                    isMatchAllConditions = isMatchAllConditions
                                ),
                                onDismiss = { onBottomSheetDataChange(null) },
                                onConfirm = { onBottomSheetDataChange(null) },
                            )
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            if (yearFilter != "전체년도" || genreFilter.isNotEmpty() || typeFilter.isNotBlank()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(APColors.White)
                        .padding(vertical = 16.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (yearFilter != "전체년도") {
                        item {
                            SelectedFilter(
                                filter = yearFilter,
                                onClick = { onCancelFilter(CancelFilterType.YEAR, null) }
                            )
                        }
                    }
                    if (quarterFilter != "전체분기") {
                        item {
                            SelectedFilter(
                                filter = quarterFilter,
                                onClick = { onCancelFilter(CancelFilterType.QUARTER, null) }
                            )
                        }
                    }
                    if (genreFilter.isNotEmpty()) {
                        items(genreFilter) {
                            SelectedFilter(
                                filter = it.name,
                                onClick = { onCancelFilter(CancelFilterType.GENRE, it.id) }
                            )
                        }
                    }
                    if (typeFilter.isNotBlank()) {
                        item {
                            SelectedFilter(
                                filter = typeFilter,
                                onClick = { onCancelFilter(CancelFilterType.TYPE, null) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box{
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(CircleShape)
                                .clickable{ onChangeSortDropdown() }
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = sortType,
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
                            offset = DpOffset(x = (-20).dp, y = 0.dp),
                            shape = RoundedCornerShape(8.dp),
                            containerColor = APColors.White,
                            shadowElevation = 2.dp,
                        ) {
                            Text(
                                text = "인기순",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = if (sortType == "인기순") APColors.Black else APColors.TextGray,
                                modifier = Modifier
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        onChangeSortDropdown()
                                        onChangeSortType("인기순")
                                    }
                                    .padding(horizontal = 27.dp, vertical = 14.dp)
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                                thickness = 1.dp,
                                color = APColors.Surface
                            )
                            Text(
                                text = "평점순",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = if (sortType == "평점순") APColors.Black else APColors.TextGray,
                                modifier = Modifier
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        onChangeSortDropdown()
                                        onChangeSortType("평점순")
                                    }
                                    .padding(horizontal = 27.dp, vertical = 14.dp)
                            )
                        }
                    }
                }
                when (uiState) {
                    is ExploreUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is ExploreUiState.Success -> {
                        val gridState = rememberLazyGridState()

                        LaunchedEffect(gridState, animes.size) {
                            snapshotFlow { gridState.layoutInfo }
                                .map { layoutInfo ->
                                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                                    val totalItemsCount = layoutInfo.totalItemsCount

                                    lastVisibleItemIndex >= totalItemsCount - 6
                                }
                                .distinctUntilChanged()
                                .collect { shouldLoadMore ->
                                    if (shouldLoadMore && !isLoading && animes.isNotEmpty()) {
                                        onLoadMoreAnimes(exploreResponse?.cursor?.lastId)
                                    }
                                }
                        }

                        if (animes.isNotEmpty()) {
                            val cardWidth = calculateCardWidth(maxWidth = 115.dp)
                            val spacing = calculateItemSpacing(itemWidth = cardWidth)
                            LazyVerticalGrid(
                                state = gridState,
                                columns = GridCells.Fixed(3),
                                horizontalArrangement = Arrangement.spacedBy(spacing),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier
                                    .fillMaxSize(),
                            ) {
                                items(animes) { anime ->
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
                        } else {
                            APEmptyContent(
                                comment = "앗! 해당 조건에 맞는 작품이 없네요.",
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                    }
                    is ExploreUiState.Error -> {}
                }
            }
        }
    }

    bottomSheetData?.let {
        APExpireBottomSheet(
            onDismiss = { it.onDismiss() },
            onApply = { params -> onApply(params) },
            metaData = metaData,
            allowMultipleSelection = true,
            initData = it.initData,
            initFilterType = it.type,
        )
    }
}

@Composable
private fun SelectedFilter(
    filter: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .size(width = 76.dp, height = 30.dp)
            .background(Color(0x335CC398), CircleShape)
            .clip(CircleShape)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = filter,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            color = APColors.Primary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = painterResource(R.drawable.ic_close),
            contentDescription = null,
            modifier = Modifier
                .size(15.dp),
            tint = APColors.Primary
        )
    }
}