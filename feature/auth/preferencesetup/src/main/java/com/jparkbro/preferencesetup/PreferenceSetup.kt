package com.jparkbro.preferencesetup

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.jparkbro.model.auth.PreferenceResponse
import com.jparkbro.model.auth.RatedAnime
import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.common.MetaData
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.ui.APBaseTextField
import com.jparkbro.ui.APBaseTopAppBar
import com.jparkbro.ui.APExpireBottomSheet
import com.jparkbro.ui.APFilterTriggerChip
import com.jparkbro.ui.DraggableStarRating
import com.jparkbro.ui.FilterParams
import com.jparkbro.ui.FilterType
import com.jparkbro.ui.R
import com.jparkbro.ui.SheetData
import com.jparkbro.ui.theme.APColors
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun PreferenceSetup(
    metaData: MetaData,
    onNavigateToHome: () -> Unit,
    viewModel: PreferenceSetupViewModel = hiltViewModel()
) {
    val bottomSheetData by viewModel.bottomSheetData.collectAsState()

    val searchText by viewModel.searchText.collectAsState()
    val yearFilter by viewModel.yearFilter.collectAsState()
    val quarterFilter by viewModel.quarterFilter.collectAsState()
    val genreFilter by viewModel.genreFilter.collectAsState()

    val searchResponse by viewModel.searchResponse.collectAsState()
    val animes by viewModel.animes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val ratedAnimes by viewModel.ratedAnime.collectAsState()

    PreferenceSetup(
        metaData = metaData,
        searchText = searchText,
        yearFilter = yearFilter,
        quarterFilter = quarterFilter,
        genreFilter = genreFilter,
        bottomSheetData = bottomSheetData,
        searchResponse = searchResponse,
        animes = animes,
        isLoading = isLoading,
        ratedAnimes = ratedAnimes,
        onSearch = viewModel::loadAnimeData,
        onRatingAdd = viewModel::addRateAnime,
        onRatingRemove = viewModel::removeRateAnime,
        onSubmitReviews = viewModel::submitReviews,
        onBottomSheetDataChange = viewModel::updateBottomSheetData,
        onChangeSearchText = viewModel::updateSearch,
        onApply = viewModel::searchByFilters,
        onNavigateToHome = onNavigateToHome,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreferenceSetup(
    metaData: MetaData = MetaData(),
    searchText: String = "",
    yearFilter: String = "",
    quarterFilter: String = "",
    genreFilter: ResponseMap = ResponseMap(),
    bottomSheetData: SheetData? = null,
    searchResponse: PreferenceResponse? = null,
    animes: List<DefaultAnime> = emptyList(),
    isLoading: Boolean = false,
    ratedAnimes: List<RatedAnime> = emptyList(),
    onSearch: (Int?) -> Unit,
    onRatingAdd: (RatedAnime) -> Unit = {},
    onRatingRemove: (Int) -> Unit = {},
    onSubmitReviews: (skip: Boolean) -> Unit,
    onBottomSheetDataChange: (SheetData?) -> Unit = { null },
    onChangeSearchText: (String) -> Unit = {},
    onApply: (params: FilterParams) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            APBaseTopAppBar(
                actions = {
                    TextButton(
                        onClick = {
                            onSubmitReviews(true)
                            onNavigateToHome()
                        }
                    ) {
                        Text(
                            text = "건너뛰기",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFFC9C9C9)
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                focusManager.clearFocus()
            },
        containerColor = Color.White,
    ) { innerPadding ->
        val listState = rememberLazyListState()

        LaunchedEffect(listState, animes.size) {
            snapshotFlow { listState.layoutInfo }
                .map { layoutInfo ->
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    val totalItemsCount = layoutInfo.totalItemsCount

                    lastVisibleItemIndex >= totalItemsCount - 2
                }
                .distinctUntilChanged()
                .collect { shouldLoadMore ->
                    if (shouldLoadMore && !isLoading && animes.isNotEmpty()) {
                        onSearch(searchResponse?.cursor?.lastId)
                    }
                }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    text = "좋아하는 애니메이션을 평가해주세요.\n취향에 맞는 작품을 추천할게요.",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    color = APColors.Black,
                )
                Text(
                    text = "좋아하는 애니메이션을 골라 주세요.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Secondary,
                )
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 20.dp, bottom = 40.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "평가한 작품 ${ratedAnimes.size}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.Gray
                        )
                        APBaseTextField(
                            value = searchText,
                            onValueChange = { onChangeSearchText(it) },
                            placeholder = {
                                Text(
                                    text = "검색",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W500,
                                    color = APColors.TextGray
                                )
                            },
                            keyboardActions = KeyboardActions(
                                onDone = { onSearch(null) }
                            ),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_search_outline),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .size(16.dp),
                                    tint = APColors.TextGray
                                )
                            },
                            trailingIcon = {
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
                                                onSearch(null)
                                            }
                                            .padding(4.dp)
                                            .size(16.dp),
                                        tint = APColors.TextGray
                                    )
                                }
                            }
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            APFilterTriggerChip(
                                title = if (yearFilter == "전체년도") "년도" else yearFilter,
                                isSelected = yearFilter != "전체년도",
                                onClick = {
                                    onBottomSheetDataChange(
                                        SheetData(
                                            type = FilterType.YEAR_AND_QUARTER,
                                            initData = FilterParams(
                                                year = yearFilter,
                                                quarter = quarterFilter,
                                                genres = if (genreFilter.id != -1) listOf(genreFilter) else emptyList(),
                                            ),
                                            onDismiss = { onBottomSheetDataChange(null) },
                                            onConfirm = { onBottomSheetDataChange(null) },
                                        )
                                    )
                                    focusManager.clearFocus()
                                }
                            )
                            APFilterTriggerChip(
                                title = if (quarterFilter == "전체분기") "분기" else quarterFilter,
                                isSelected = quarterFilter != "전체분기",
                                onClick = {
                                    onBottomSheetDataChange(
                                        SheetData(
                                            type = FilterType.YEAR_AND_QUARTER,
                                            initData = FilterParams(
                                                year = yearFilter,
                                                quarter = quarterFilter,
                                                genres = if (genreFilter.id != -1) listOf(genreFilter) else emptyList(),
                                            ),
                                            onDismiss = { onBottomSheetDataChange(null) },
                                            onConfirm = { onBottomSheetDataChange(null) },
                                        )
                                    )
                                    focusManager.clearFocus()
                                }
                            )
                            APFilterTriggerChip(
                                title = if (genreFilter.id == -1) "장르" else genreFilter.name,
                                isSelected = genreFilter.id != -1,
                                onClick = {
                                    onBottomSheetDataChange(
                                        SheetData(
                                            type = FilterType.GENRE,
                                            initData = FilterParams(
                                                year = yearFilter,
                                                quarter = quarterFilter,
                                                genres = if (genreFilter.id != -1) listOf(genreFilter) else emptyList(),
                                            ),
                                            onDismiss = { onBottomSheetDataChange(null) },
                                            onConfirm = { onBottomSheetDataChange(null) },
                                        )
                                    )
                                    focusManager.clearFocus()
                                }
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 8.dp),
                        thickness = 2.dp,
                        color = APColors.Surface
                    )
                }
                if (animes.isNotEmpty()) {
                    items(animes) { anime ->
                        val ratedAnime = ratedAnimes.find { it.animeId == anime.animeId }

                        AnimeRatingCard(
                            modifier = Modifier
                                .padding(horizontal = 20.dp),
                            initRating = ratedAnime?.rating ?: 0f,
                            anime = anime,
                            onRatingAdd = onRatingAdd,
                            onRatingRemove = onRatingRemove,
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
            Column(
                modifier = Modifier
                    .height(103.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(bottom = 30.dp),
                    color = APColors.Gray
                )
                Button(
                    onClick = {
                        // 1. 서버에 데이터 전송
                        onSubmitReviews(false)
                        // 2. Home
                        onNavigateToHome()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = APColors.Primary,
                        disabledContainerColor = APColors.Gray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.dp)
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "완료",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                        color = Color(0xFFFFFFFF)
                    )
                }
            }
        }
    }

    bottomSheetData?.let {
        APExpireBottomSheet(
            onDismiss = { it.onDismiss() },
            initFilterType = it.type,
            metaData = metaData,
            includeTypeFilter = false,
            initData = it.initData,
            onApply = { params -> onApply(params) }
        )
    }
}

@Composable
private fun AnimeRatingCard(
    modifier: Modifier = Modifier,
    initRating: Float,
    anime: DefaultAnime,
    onRatingAdd: (RatedAnime) -> Unit = {},
    onRatingRemove: (Int) -> Unit = {},
) {
    var rating by remember(initRating) { mutableFloatStateOf(initRating) }
    var showRating by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, APColors.Surface, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .clickable { showRating = !showRating }
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = anime.coverImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(width = 133.dp, height = 89.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${anime.title}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.Black,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (initRating != 0f) {
                        TextButton(
                            onClick = { onRatingRemove(anime.animeId) },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = APColors.Primary,
                                disabledContainerColor = APColors.Gray
                            ),
                            enabled = rating != 0f,
                            contentPadding = PaddingValues(horizontal = 11.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "평가취소",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W500,
                            )
                        }
                    }
                }
                if (anime.genres.isNotEmpty()) {
                    Row {
                        anime.genres.forEach { genre ->
                            Text(
                                text = genre,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.TextGray
                            )
                        }
                    }
                }
                if (initRating != 0f) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
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
                        Text(
                            text = "(${initRating}/5.0)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.Point
                        )
                    }
                }
            }
        }
        if (showRating) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(APColors.Surface)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DraggableStarRating(
                    starSize = 28.dp,
                    initialRating = rating,
                    onRatingChanged = { rating = it },
                )
                Text(
                    text = "(${rating}/5.0)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = if (rating == 0f) APColors.Gray else APColors.Point
                )
                TextButton(
                    onClick = {
                        onRatingAdd(
                            RatedAnime(
                                animeId = anime.animeId,
                                rating = rating
                            )
                        )
                        showRating = false
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = APColors.Primary,
                        disabledContainerColor = APColors.Gray
                    ),
                    enabled = rating != 0f
                ) {
                    Text(
                        text = "평가하기",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
    }
}