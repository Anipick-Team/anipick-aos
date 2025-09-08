package com.jparkbro.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.jparkbro.model.common.MetaData
import com.jparkbro.model.common.ResponseMap
import com.jparkbro.model.ranking.RankingItem
import com.jparkbro.model.ranking.RankingRequest
import com.jparkbro.model.ranking.RankingTrend
import com.jparkbro.model.ranking.RankingType
import com.jparkbro.ui.APExpireBottomSheet
import com.jparkbro.ui.APLogoSearchTopAppBar
import com.jparkbro.ui.FilterParams
import com.jparkbro.ui.FilterType
import com.jparkbro.ui.R
import com.jparkbro.ui.SheetData
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.util.extension.toImageModel

@Composable
internal fun Ranking(
    metaData: MetaData,
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    viewModel: RankingViewModel = hiltViewModel()
) {
    val params by viewModel.params.collectAsState()
    val bottomSheetData by viewModel.bottomSheetData.collectAsState()

    val uiState by viewModel.uiState.collectAsState()

    Ranking(
        uiState = uiState,
        metaData = metaData,
        bottomNav = bottomNav,
        onNavigateToSearch = onNavigateToSearch,
        params = params,
        onChangeFilter = viewModel::updateFilter,
        bottomSheetData = bottomSheetData,
        onChangeSheetData = viewModel::updateBottomSheetData,
        onNavigateToAnimeDetail = onNavigateToAnimeDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Ranking(
    uiState: RankingUiState = RankingUiState.Loading,
    metaData: MetaData,
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    params: RankingRequest,
    onChangeFilter: (RankingRequest) -> Unit = {},
    bottomSheetData: SheetData? = null,
    onChangeSheetData: (SheetData?) -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
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
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(9.dp))
            Column(
                modifier = Modifier
                    .background(Color(0xFFFFFFFF))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(78.dp)
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "실시간",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFFFFFFFF),
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    if (params.type != RankingType.REAL_TIME) {
                                        onChangeFilter(RankingRequest(type = RankingType.REAL_TIME))
                                    }
                                }
                                .background(if (params.type == RankingType.REAL_TIME) APColors.Primary else APColors.Gray, CircleShape)
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                        val yearSeason = if (params.type == RankingType.YEAR_SEASON) {
                            if (params.year == "전체년도" || params.season == "전체분기") {
                                "${params.year}"
                            } else if (params.year == null) {
                                "전체년도"
                            } else if (params.season == null) {
                                "${params.year}"
                            } else {
                                "${params.year}/${params.season}"
                            }
                        } else {
                            "년도/분기"
                        }
                        Text(
                            text = yearSeason,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFFFFFFFF),
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    onChangeSheetData(
                                        SheetData(
                                            type = FilterType.YEAR_AND_QUARTER,
                                            initData = FilterParams(
                                                year = params.year ?: "전체년도",
                                                quarter = params.season ?: "전체분기"
                                            ),
                                            onDismiss = { onChangeSheetData(null) },
                                            onConfirm = {
                                                onChangeFilter(
                                                    RankingRequest(
                                                        type = RankingType.YEAR_SEASON,
                                                        year = it.year,
                                                        season = it.quarter
                                                    )
                                                )
                                            }
                                        )
                                    )
                                }
                                .background(if (params.type == RankingType.YEAR_SEASON) APColors.Primary else APColors.Gray, CircleShape)
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                        Text(
                            text = "역대",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFFFFFFFF),
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    if (params.type != RankingType.ALL_TIME) {
                                        onChangeFilter(RankingRequest(type = RankingType.ALL_TIME))
                                    }
                                }
                                .background(if (params.type == RankingType.ALL_TIME) APColors.Primary else APColors.Gray, CircleShape)
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                onChangeSheetData(
                                    SheetData(
                                        type = FilterType.GENRE,
                                        initData = FilterParams(genres = params.genre?.let { listOf(it) } ?: emptyList()),
                                        onDismiss = { onChangeSheetData(null) },
                                        onConfirm = {
                                            onChangeFilter(
                                                RankingRequest(
                                                    type = params.type,
                                                    genre = if (it.genres.isNotEmpty()) it.genres[0] else ResponseMap(),
                                                )
                                            )
                                        }
                                    )
                                )
                            }
                            .border(1.dp, if (params.genre?.id != -1) APColors.Secondary else APColors.LightGray, CircleShape)
                            .padding(horizontal = 15.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = params.genre?.name?.ifBlank { "장르" } ?: "장르",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W400,
                            color = if (params.genre?.id != -1) APColors.Secondary else APColors.Black
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_down),
                            contentDescription = null,
                            tint = if (params.genre?.id != -1) APColors.Secondary else APColors.Gray,
                            modifier = Modifier
                                .size(15.dp)
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 2.dp,
                    color = APColors.Surface
                )
            }
            when (uiState) {
                is RankingUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(APColors.White),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is RankingUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(APColors.White)
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(top = 20.dp)
                    ) {
                        items(uiState.item.animes) { anime ->
                            RankingItem(
                                anime = anime,
                                onClick = { onNavigateToAnimeDetail(it) }
                            )
                        }
                    }
                }

                is RankingUiState.Error -> {}
            }
        }
    }

    bottomSheetData?.let {
        APExpireBottomSheet(
            onDismiss = { it.onDismiss() },
            initFilterType = it.type,
            metaData = metaData,
            includeYearQuarter = it.type == FilterType.YEAR_AND_QUARTER,
            includeGenres = it.type == FilterType.GENRE,
            includeTypeFilter = false,
            initData = it.initData,
            onApply = { filter ->
                it.onConfirm(filter)
            }
        )
    }
}

@Composable
private fun RankingItem(
    anime: RankingItem,
    onClick: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick(anime.animeId) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .width(40.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "${anime.rank}",
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
                color = APColors.Black
            )
            when (anime.trend) {
                RankingTrend.UP, RankingTrend.DOWN, RankingTrend.SAME -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(
                                when (anime.trend) {
                                    RankingTrend.UP -> R.drawable.ic_ranking_up
                                    RankingTrend.DOWN -> R.drawable.ic_ranking_down
                                    RankingTrend.SAME -> R.drawable.ic_ranking_hold
                                    else -> R.drawable.ic_ranking_hold
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(8.dp),
                            tint = when (anime.trend) {
                                RankingTrend.UP -> APColors.Point
                                RankingTrend.DOWN -> APColors.Secondary
                                RankingTrend.SAME -> APColors.TextGray
                                else -> APColors.TextGray
                            }
                        )
                        Text(
                            text = "${anime.change}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = when (anime.trend) {
                                RankingTrend.UP -> APColors.Point
                                RankingTrend.DOWN -> APColors.Secondary
                                RankingTrend.SAME -> APColors.TextGray
                                else -> APColors.TextGray
                            }
                        )
                    }
                }
                else -> {}
            }
        }
        AsyncImage(
            model = anime.coverImageUrl.toImageModel(),
            contentDescription = null,
            modifier = Modifier
                .size(width = 128.dp, height = 182.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "${anime.title}",
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = APColors.Black,
                lineHeight = (1.4).em,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                anime.genres.forEach { genre ->
                    Text(
                        text = genre,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.Primary,
                        modifier = Modifier
                            .background(Color(0x1A5CC398), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RankingPreview() {
    Ranking(
        metaData = MetaData(),
        bottomNav = {},
        onNavigateToSearch = {},
        onNavigateToAnimeDetail = {}
    )
}
