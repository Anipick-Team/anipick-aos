package com.jparkbro.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jparkbro.model.common.DefaultAnime
import com.jparkbro.model.home.ComingSoonItem
import com.jparkbro.model.home.ContentType
import com.jparkbro.model.home.HomeRecommendResponse
import com.jparkbro.model.home.HomeReviewItem
import com.jparkbro.model.home.UpcomingSeasonItems
import com.jparkbro.ui.APCardItem
import com.jparkbro.ui.APLogoSearchTopAppBar
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors

@Composable
internal fun Home(
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateToRanking: () -> Unit,
    onNavigateToExplore: (year: String?, quarter: String?) -> Unit,
    onNavigateToHomeDetail: (ContentType) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val nickname by viewModel.nickname.collectAsState()

    val trendItems by viewModel.trendItems.collectAsState()
    val recommend by viewModel.recommend.collectAsState()
    val recentRecommend by viewModel.recentRecommend.collectAsState()
    val recentReviews by viewModel.recentReviews.collectAsState()
    val upcomingSeasonItems by viewModel.upcomingSeasonItems.collectAsState()
    val comingSoonItems by viewModel.comingSoonItems.collectAsState()

    Home(
        bottomNav = bottomNav,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToAnimeDetail = onNavigateToAnimeDetail,
        onNavigateToRanking = onNavigateToRanking,
        onNavigateToExplore = onNavigateToExplore,
        onNavigateToHomeDetail = onNavigateToHomeDetail,
        nickname = nickname,
        trendItems = trendItems,
        recommend = recommend,
        recentRecommend = recentRecommend,
        recentReviews = recentReviews,
        upcomingSeasonItem = upcomingSeasonItems,
        comingSoonItem = comingSoonItems
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Home(
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateToRanking: () -> Unit,
    onNavigateToExplore: (year: String?, quarter: String?) -> Unit,
    onNavigateToHomeDetail: (ContentType) -> Unit,
    nickname: String,
    trendItems: HomeUiState = HomeUiState.Loading,
    recommend: HomeUiState = HomeUiState.Loading,
    recentRecommend: HomeUiState = HomeUiState.Loading,
    recentReviews: HomeUiState = HomeUiState.Loading,
    upcomingSeasonItem: HomeUiState = HomeUiState.Loading,
    comingSoonItem: HomeUiState = HomeUiState.Loading
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            /** 실시간 인기 애니메이션 */
            when (trendItems) {
                is HomeUiState.Loading -> { LoadingBox() }
                is HomeUiState.Success<*> -> {
                    val data = trendItems.data
                    if (data is List<*>) {
                        val animes = data.filterIsInstance<DefaultAnime>()
                        CategorySection(
                            title = "실시간 인기 애니메이션",
                            itemList = {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(animes) { anime ->
                                        APCardItem(
                                            title = "${anime.title}",
                                            imageUrl = anime.coverImageUrl,
                                            rank = "${anime.rank}",
                                            cardWidth = 128.dp,
                                            cardHeight = 182.dp,
                                            fontSize = 16.sp,
                                            maxLine = 2,
                                            onClick = { onNavigateToAnimeDetail(anime.animeId) }
                                        )
                                    }
                                }
                            },
                            onNavigateClick = { onNavigateToRanking() }
                        )
                    }
                }
                is HomeUiState.Error -> { }
            }
            /** 오늘의 추천작 */
            when (recommend) {
                is HomeUiState.Loading -> { LoadingBox() }
                is HomeUiState.Success<*> -> {
                    val data = recommend.data as HomeRecommendResponse
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 20.dp)
                            .padding(top = 32.dp, bottom = 26.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (data.animes.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.recommend_null_1),
                                    contentDescription = null
                                )
                            }
                        } else {
                            if (data.referenceAnimeTitle == null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 20.dp),
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "오늘의 추천작, ",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.W600,
                                                color = APColors.Black,
                                                lineHeight = (1.4).em
                                            )
                                            Text(
                                                text = nickname,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.W600,
                                                color = APColors.Black,
                                                lineHeight = (1.4).em,
                                                overflow = TextOverflow.Ellipsis,
                                                maxLines = 1,
                                                modifier = Modifier.weight(1f, fill = false)
                                            )
                                            Text(
                                                text = " 님의",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.W600,
                                                color = APColors.Black,
                                                lineHeight = (1.4).em
                                            )
                                        }
                                        Text(
                                            text = "취향에 맞춰 준비했어요!",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.W600,
                                            color = APColors.Black,
                                            lineHeight = (1.4).em
                                        )
                                    }
                                    Icon(
                                        painter = painterResource(R.drawable.ic_chevron_right),
                                        contentDescription = null,
                                        tint = APColors.Gray,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .clickable { onNavigateToHomeDetail(ContentType.RECOMMEND) }
                                            .padding(4.dp)
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 20.dp),
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${data.referenceAnimeTitle}",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.W600,
                                                color = APColors.Black,
                                                lineHeight = (1.4).em,
                                                overflow = TextOverflow.Ellipsis,
                                                maxLines = 1,
                                                modifier = Modifier.weight(1f, fill = false)
                                            )
                                            Text(
                                                text = " 를 재밌게 보셨다면,",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.W600,
                                                color = APColors.Black,
                                                lineHeight = (1.4).em
                                            )
                                        }
                                        Text(
                                            text = "이 작품들도 마음에 드실 거에요!",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.W600,
                                            color = APColors.Black,
                                            lineHeight = (1.4).em
                                        )
                                    }
                                    Icon(
                                        painter = painterResource(R.drawable.ic_chevron_right),
                                        contentDescription = null,
                                        tint = APColors.Gray,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .clickable { onNavigateToHomeDetail(ContentType.RECOMMEND) }
                                            .padding(4.dp)
                                    )
                                }
                            }
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(data.animes) { anime ->
                                    APCardItem(
                                        title = "${anime.title}",
                                        imageUrl = anime.coverImageUrl,
                                        cardWidth = 128.dp,
                                        cardHeight = 182.dp,
                                        fontSize = 16.sp,
                                        maxLine = 2,
                                        onClick = { onNavigateToAnimeDetail(anime.animeId) }
                                    )
                                }
                            }
                        }
                    }
                }
                is HomeUiState.Error -> { }
            }
            /** 최근 리뷰 */
            when (recentReviews) {
                is HomeUiState.Loading -> { LoadingBox() }
                is HomeUiState.Success<*> -> {
                    val data = recentReviews.data
                    if (data is List<*>) {
                        val review = data.filterIsInstance<HomeReviewItem>()
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent)
                                .padding(horizontal = 20.dp)
                                .padding(top = 32.dp, bottom = 26.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "최근 리뷰",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.W600,
                                    color = Color.Black,
                                )
                                Icon(
                                    painter = painterResource(R.drawable.ic_chevron_right),
                                    contentDescription = null,
                                    tint = APColors.Gray,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable { onNavigateToHomeDetail(ContentType.RECENT_REVIEW) }
                                        .padding(4.dp)
                                )
                            }
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(review) { review ->
                                    Column(
                                        modifier = Modifier
                                            .background(Color.White, RoundedCornerShape(8.dp))
                                            .width(229.dp)
                                            .height(136.dp)
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(7.dp)
                                        ) {
                                            Text(
                                                text = "${review.animeTitle}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.W500,
                                                color = APColors.Black,
                                                lineHeight = (1.25).em,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                            Text(
                                                text = review.reviewContent,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.W500,
                                                color = APColors.Black,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Text(
                                                text = review.nickname,
                                                modifier = Modifier.weight(1f, fill = false),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.W500,
                                                color = APColors.Black,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                            VerticalDivider(modifier = Modifier.height(10.dp), thickness = 1.dp, color = APColors.Black)
                                            Text(
                                                text = review.createdAt,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.W500,
                                                color = APColors.Black,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                is HomeUiState.Error -> {}
            }
            /** 다음 분기 방영 예정 애니 */
            when (upcomingSeasonItem) {
                is HomeUiState.Loading -> {
                    LoadingBox()
                }
                is HomeUiState.Success<*> -> {
                    val data = upcomingSeasonItem.data
                    if (data is UpcomingSeasonItems) {
                        CategorySection(
                            title = "${data.seasonYear}년 ${data.season}분기 방영예정",
                            itemList = {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(data.animes) { item ->
                                        APCardItem(
                                            title = "${item.title}",
                                            imageUrl = item.coverImageUrl,
                                            cardWidth = 128.dp,
                                            cardHeight = 182.dp,
                                            fontSize = 16.sp,
                                            onClick = { onNavigateToAnimeDetail(item.animeId) }
                                        )
                                    }
                                }
                            },
                            onNavigateClick = { onNavigateToExplore("${data.seasonYear}", "${data.season}분기") }
                        )
                    }
                }
                is HomeUiState.Error -> {}
            }
            /** 최근 본 작품과 비슷한 작품 */
            when (recentRecommend) {
                is HomeUiState.Loading -> { LoadingBox() }
                is HomeUiState.Success<*> -> {
                    val data = recentRecommend.data as HomeRecommendResponse
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 20.dp)
                            .padding(top = 32.dp, bottom = 26.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (data.animes.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.recommend_null_1), // TODO 이미지 변경
                                    contentDescription = null
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 20.dp),
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "최근 찾아보신 ",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.W600,
                                            color = APColors.Black,
                                            lineHeight = (1.4).em
                                        )
                                        Text(
                                            text = "${data.referenceAnimeTitle}",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.W600,
                                            color = APColors.Black,
                                            lineHeight = (1.4).em,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )
                                        Text(
                                            text = " 과",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.W600,
                                            color = APColors.Black,
                                            lineHeight = (1.4).em
                                        )
                                    }
                                    Text(
                                        text = "비슷한 작품이에요!",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.W600,
                                        color = APColors.Black,
                                        lineHeight = (1.4).em
                                    )
                                }
                                Icon(
                                    painter = painterResource(R.drawable.ic_chevron_right),
                                    contentDescription = null,
                                    tint = APColors.Gray,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable { onNavigateToHomeDetail(ContentType.RECENT_RECOMMEND) }
                                        .padding(4.dp)
                                )
                            }
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(data.animes) { anime ->
                                    APCardItem(
                                        title = "${anime.title}",
                                        imageUrl = anime.coverImageUrl,
                                        cardWidth = 128.dp,
                                        cardHeight = 182.dp,
                                        fontSize = 16.sp,
                                        maxLine = 2,
                                        onClick = { onNavigateToAnimeDetail(anime.animeId) }
                                    )
                                }
                            }
                        }
                    }
                }
                is HomeUiState.Error -> {}
            }
            /** 공개 예정 */
            when (comingSoonItem) {
                is HomeUiState.Loading -> {
                    LoadingBox()
                }
                is HomeUiState.Success<*> -> {
                    val data = comingSoonItem.data
                    if (data is List<*>) {
                        val comingSoonItems = data.filterIsInstance<ComingSoonItem>()
                        CategorySection(
                            title = "공개 예정",
                            itemList = {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(comingSoonItems) { item ->
                                        APCardItem(
                                            title = "${item.title}",
                                            imageUrl = item.coverImageUrl,
                                            cardWidth = 128.dp,
                                            cardHeight = 182.dp,
                                            fontSize = 16.sp,
                                            description = {
                                                Text(
                                                    text = "${item.releaseDate}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.W500,
                                                    color = APColors.TextGray,
                                                )
                                            },
                                            onClick = { onNavigateToAnimeDetail(item.animeId) }
                                        )
                                    }
                                }
                            },
                            onNavigateClick = { onNavigateToHomeDetail(ContentType.COMING_SOON) }
                        )
                    }
                }
                is HomeUiState.Error -> {}
            }
            Spacer(modifier = Modifier.height(43.dp))
        }
    }
}

@Composable
private fun CategorySection(
    title: String = "",
    itemList: @Composable () -> Unit,
    onNavigateClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp)
            .padding(top = 36.dp, bottom = 52.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
                color = Color.Black,
            )
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = APColors.Gray,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onNavigateClick() }
                    .padding(4.dp)
            )
        }
        itemList()
    }
}

@Composable
private fun LoadingBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            strokeWidth = 4.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    Home(
        bottomNav = {},
        onNavigateToSearch = {},
        onNavigateToAnimeDetail = {},
        onNavigateToRanking = {},
        onNavigateToExplore = {_, _ ->},
        onNavigateToHomeDetail = {},
        upcomingSeasonItem = HomeUiState.Loading,
        nickname = ""
    )
}