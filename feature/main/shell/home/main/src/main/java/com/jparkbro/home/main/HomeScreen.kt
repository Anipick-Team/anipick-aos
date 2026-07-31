package com.jparkbro.home.main

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.home.main.components.AnimesSection
import com.jparkbro.home.main.components.EmptyRecommend
import com.jparkbro.home.main.components.EmptySimilar
import com.jparkbro.home.main.components.ReviewsSection
import com.jparkbro.home.main.components.SkeletonScreen
import com.jparkbro.model.common.UiState
import com.jparkbro.model.enum.HomeDetailType
import com.jparkbro.model.enum.DialogType
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APAlertDialog
import com.jparkbro.ui.components.APAnimeCard
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APMainTopAppBar
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.DialogStyle
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.util.UiText
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.FavoriteOffIcon
import com.jparkbro.ui.theme.FavoriteOnIcon
import com.jparkbro.ui.theme.Folder
import com.jparkbro.ui.util.extension.quarterIntToString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeRoot(
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToRanking: () -> Unit,
    onNavigateToExplore: (year: String?, quarter: String?) -> Unit,
    onNavigateToHomeDetail: (HomeDetailType) -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                bottomNav = bottomNav,
                onNavigateToSearch = onNavigateToSearch
            )
        }

        UiState.Error -> {
            Scaffold(
                topBar = { APMainTopAppBar(onNavigateToSearch = onNavigateToSearch) },
                bottomBar = bottomNav,
                containerColor = AniPickSurface
            ) {
                APErrorScreen(
                    onClick = { viewModel.onAction(HomeAction.OnRetryClicked) },
                    modifier = Modifier.padding(it)
                )
            }
        }

        UiState.Success -> {
            HomeScreen(
                bottomNav = bottomNav,
                state = state,
                onAction = { action ->
                    when (action) {
                        HomeAction.NavigateToSearch -> onNavigateToSearch()
                        is HomeAction.NavigateToAnimeDetail -> {
                            onNavigateToInfoAnime(action.animeId)
                        }

                        is HomeAction.NavigateToNextQuarter -> {
                            onNavigateToExplore(action.year, action.quarter)
                        }

                        HomeAction.NavigateToTrending -> onNavigateToRanking()
                        HomeAction.NavigateToRecommend -> onNavigateToHomeDetail(HomeDetailType.RECOMMENDS)
                        HomeAction.NavigateToReview -> onNavigateToHomeDetail(HomeDetailType.RECENT_REVIEWS)
                        HomeAction.NavigateToSimilar -> onNavigateToHomeDetail(HomeDetailType.SIMILAR_TO_WATCHED)
                        HomeAction.NavigateToUpcoming -> onNavigateToHomeDetail(HomeDetailType.UPCOMING_RELEASE)
                        HomeAction.NavigateToInstagram -> {
                            val intent = Intent(Intent.ACTION_VIEW, "https://www.instagram.com/anipick.official/".toUri())
                            context.startActivity(intent)
                        }
                    }
                    viewModel.onAction(action)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    bottomNav: @Composable () -> Unit,
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    Scaffold(
        topBar = { APMainTopAppBar(onNavigateToSearch = { onAction(HomeAction.NavigateToSearch) }) },
        bottomBar = bottomNav,
        containerColor = AniPickSurface
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            stickyHeader {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = dimensionResource(R.dimen.border_width_default),
                    color = AniPickSurface
                )
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                ) {
                    /*Image(
                        imageVector = ImageVector.vectorResource(R.drawable.instagram_banner),
                        contentDescription = "Move Instagram Banner",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                onAction(HomeAction.NavigateToInstagram)
                            }
                    )*/
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Folder,
                            contentDescription = "Show Notice",
                            modifier = Modifier
                                .size(64.dp)
                                .background(color = Color(0xFFE3E3E3), shape = CircleShape)
                                .border(width = 1.dp, color = Color(0xFFCCCCCC), shape = CircleShape)
                                .padding(16.dp)
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "데이터 롤백 안내",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W700,
                                    color = Color.Black,
                                )
                                Text(
                                    text = "자세히 보기",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.W600,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(Color.Black, CircleShape)
                                        .clickable { onAction(HomeAction.ShowNotice) }
                                        .padding(horizontal = 12.dp, vertical = 2.dp)

                                )
                            }
                            BoxWithConstraints {
                                val noticeTextStyle = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.W400)
                                val firstSentence = "서버 복구 과정에서 일부 데이터가 롤백되었음을 안내 드립니다."
                                val secondSentence = "서비스 이용에 큰 불편을 드린 점 진심으로 사과드립니다."

                                val textMeasurer = rememberTextMeasurer()
                                val density = LocalDensity.current
                                val availableWidthPx = with(density) { maxWidth.toPx() }
                                val firstSentenceWidthPx = remember(firstSentence, noticeTextStyle) {
                                    textMeasurer.measure(firstSentence, style = noticeTextStyle).size.width
                                }

                                // 첫 문장이 한 줄에 다 들어갈 만큼 화면이 넓으면 문장 사이에 강제 개행,
                                // 그렇지 않고 첫 문장 안에서 이미 자연 개행이 일어나면 강제 개행 생략
                                val noticeText = if (firstSentenceWidthPx <= availableWidthPx) {
                                    "$firstSentence\n$secondSentence"
                                } else {
                                    "$firstSentence $secondSentence"
                                }

                                Text(
                                    text = noticeText,
                                    style = noticeTextStyle,
                                    color = Color.Black,
                                )
                            }
                        }
                    }
                    if (state.trendingAnimeDtos.isNotEmpty()) {
                        TrendingAnimes(
                            state = state,
                            onAction = onAction
                        )
                    }
                    if (state.recommendedAnimes.isNotEmpty()) {
                        RecommendedToday(
                            state = state,
                            onAction = onAction
                        )
                    } else {
                        EmptyRecommend()
                    }
                    if (state.recentReviews.isNotEmpty()) {
                        RecentReviews(
                            state = state,
                            onAction = onAction
                        )
                    }
                    if (state.nextQuarterAnimes.isNotEmpty()) {
                        NextQuarter(
                            state = state,
                            onAction = onAction
                        )
                    }
                    if (state.similarAnimes.isNotEmpty()) {
                        SimilarToWatched(
                            state = state,
                            onAction = onAction
                        )
                    } else {
                        EmptySimilar()
                    }
                    if (state.upcomingAnimes.isNotEmpty()) {
                        UpcomingReleases(
                            state = state,
                            onAction = onAction
                        )
                    }
                }
            }
        }
    }

    if (state.showNoticeDialog) {
        APAlertDialog(
            dialogData = DialogData(
                type = DialogType.ALERT,
                title = UiText.DynamicString("서버 복구 과정 중\n데이터 롤백 및 재가입 안내"),
                subTitle = UiText.DynamicString("7월 7일서버 복구 과정에서 데이터가 7월 4일 기준으로 롤백되는 문제가 발생했습니다.\n\n" +
                        "이로 인해 7월 4일 이후에 작성된 리뷰, 평가, 시청 기록 등의 데이터가 복구되지 않았으며, 해당 기간에 가입하신 계정 정보 또한 손실되었습니다.\n\n" +
                        "7월 4일 ~ 7월 7일 가입하신 회원분들은 번거러우시겠지만 다시 가입을 진행해 주시기 바랍니다.\n\n" +
                        "현재 동일한 문제가 재발하지 않도록 서버 증설 및 시스템 개선 작업을 진행하고 있습니다.\n\n" +
                        "서비스 이용에 큰 불편을 드린 점 진심으로 사과드립니다."),
                confirm = UiText.DynamicString("닫기"),
                onConfirm = { onAction(HomeAction.DismissNotice) },
                onDismiss = { onAction(HomeAction.DismissNotice) }
            ),
            style = DialogStyle(
                subTitleAlign = TextAlign.Start
            )
        )
    }
}

@Composable
private fun TrendingAnimes(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    AnimesSection(
        title = {
            Text(
                text = stringResource(R.string.home_main_popular_now),
                style = AniPick20Bold.copy(color = AniPickBlack)
            )
        },
        itemList = {
            LazyRow(
                contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
            ) {
                items(state.trendingAnimeDtos) { anime ->
                    APAnimeCard(
                        cardWidth = 128.dp,
                        imageUrl = anime.coverImageUrl,
                        rank = anime.rank,
                        title = anime.titleKor ?: anime.titleEng ?: anime.title,
                        maxLine = 2,
                        onClick = { onAction(HomeAction.NavigateToAnimeDetail(anime.animeId ?: -1)) }
                    )
                }
            }
        },
        onNavigateClick = { onAction(HomeAction.NavigateToTrending) }
    )
}

@Composable
private fun RecommendedToday(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    AnimesSection(
        title = {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = dimensionResource(R.dimen.padding_large)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_small))
            ) {
                if (state.referenceAnimeTitle == null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.home_main_recommended_prefix),
                            style = AniPick20Bold.copy(color = AniPickBlack)
                        )
                        Text(
                            text = state.nickname,
                            style = AniPick20Bold.copy(color = AniPickBlack),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Text(
                            text = stringResource(R.string.home_main_recommended_mid),
                            style = AniPick20Bold.copy(color = AniPickBlack)
                        )
                    }
                    Text(
                        text = stringResource(R.string.home_main_recommended_suffix),
                        style = AniPick20Bold.copy(color = AniPickBlack)
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = state.referenceAnimeTitle,
                            style = AniPick20Bold.copy(color = AniPickBlack),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Text(
                            text = stringResource(R.string.home_main_recommended_anime_prefix),
                            style = AniPick20Bold.copy(color = AniPickBlack)
                        )
                    }
                    Text(
                        text = stringResource(R.string.home_main_recommended_anime_suffix),
                        style = AniPick20Bold.copy(color = AniPickBlack)
                    )
                }
            }
        },
        itemList = {
            LazyRow(
                contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
            ) {
                items(state.recommendedAnimes) { anime ->
                    APAnimeCard(
                        cardWidth = 128.dp,
                        imageUrl = anime.coverImageUrl,
                        title = anime.title,
                        maxLine = 2,
                        onClick = { onAction(HomeAction.NavigateToAnimeDetail(anime.animeId ?: -1)) }
                    )
                }
            }
        },
        onNavigateClick = { onAction(HomeAction.NavigateToRecommend) }
    )
}

@Composable
private fun RecentReviews(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    ReviewsSection(
        itemList = {
            LazyRow(
                contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
            ) {
                items(state.recentReviews) { review ->
                    Column(
                        modifier = Modifier
                            .background(AniPickWhite, AniPickSmallShape)
                            .width(228.dp)
                            .height(136.dp)
                            .padding(horizontal = dimensionResource(R.dimen.padding_default), vertical = dimensionResource(R.dimen.padding_medium)),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                        ) {
                            review.animeTitle?.let {
                                Text(
                                    text = it,
                                    style = AniPick12Normal.copy(color = AniPickBlack),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                            review.content?.let {
                                Text(
                                    text = it,
                                    style = AniPick16Normal.copy(color = AniPickBlack),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            review.nickname?.let {
                                Text(
                                    text = it,
                                    style = AniPick12Normal.copy(color = AniPickBlack),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false),
                                )
                            }
                            VerticalDivider(modifier = Modifier.height(10.dp), thickness = dimensionResource(R.dimen.border_width_default), color = AniPickBlack)
                            review.createdAt?.let {
                                Text(
                                    text = it,
                                    style = AniPick12Normal.copy(color = AniPickBlack)
                                )
                            }
                        }
                    }
                }
            }
        },
        onNavigateClick = { onAction(HomeAction.NavigateToReview) }
    )
}

@Composable
private fun NextQuarter(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    AnimesSection(
        title = {
            Text(
                text = stringResource(R.string.home_main_next_quarter, state.year, state.season),
                style = AniPick20Bold.copy(color = AniPickBlack)
            )
        },
        itemList = {
            LazyRow(
                contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
            ) {
                items(state.nextQuarterAnimes) { anime ->
                    APAnimeCard(
                        cardWidth = 128.dp,
                        imageUrl = anime.coverImageUrl,
                        title = anime.title,
                        maxLine = 2,
                        onClick = { onAction(HomeAction.NavigateToAnimeDetail(anime.animeId ?: -1)) }
                    )
                }
            }
        },
        onNavigateClick = { onAction(HomeAction.NavigateToNextQuarter(state.year.toString(), state.season.quarterIntToString() ?: "1분기")) }
    )
}

@Composable
private fun SimilarToWatched(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(state.similarAnimes) {
        if (state.similarAnimes.isNotEmpty()) {
            lazyListState.animateScrollToItem(0)
        }
    }

    AnimesSection(
        title = {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = dimensionResource(R.dimen.padding_large)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_small))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.home_main_similar_to_watched_prefix),
                        style = AniPick20Bold.copy(color = AniPickBlack)
                    )
                    state.similarAnimeTitle?.let {
                        Text(
                            text = it,
                            style = AniPick20Bold.copy(color = AniPickBlack),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }
                    Text(
                        text = stringResource(R.string.home_main_similar_to_watched_mid),
                        style = AniPick20Bold.copy(color = AniPickBlack)
                    )
                }
                Text(
                    text = stringResource(R.string.home_main_similar_to_watched_suffix),
                    style = AniPick20Bold.copy(color = AniPickBlack)
                )
            }
        },
        itemList = {
            LazyRow(
                state = lazyListState,
                contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
            ) {
                items(state.similarAnimes) { anime ->
                    APAnimeCard(
                        cardWidth = 128.dp,
                        imageUrl = anime.coverImageUrl,
                        title = anime.title,
                        maxLine = 2,
                        onClick = { onAction(HomeAction.NavigateToAnimeDetail(anime.animeId ?: -1)) }
                    )
                }
            }
        },
        onNavigateClick = { onAction(HomeAction.NavigateToSimilar) }
    )
}

@Composable
private fun UpcomingReleases(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    AnimesSection(
        title = {
            Text(
                text = stringResource(R.string.home_main_upcoming_release),
                style = AniPick20Bold.copy(color = AniPickBlack)
            )
        },
        itemList = {
            LazyRow(
                contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
            ) {
                items(state.upcomingAnimes) { anime ->
                    APAnimeCard(
                        cardWidth = 128.dp,
                        imageUrl = anime.coverImageUrl,
                        title = anime.title,
                        maxLine = 2,
                        description = {
                            anime.releaseDate?.let {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text(
                                        text = it,
                                        style = AniPick12Normal.copy(color = AniPickGray400),
                                    )
                                }
                            }
                        },
                        onClick = { onAction(HomeAction.NavigateToAnimeDetail(anime.animeId ?: -1)) }
                    )
                }
            }
        },
        onNavigateClick = { onAction(HomeAction.NavigateToUpcoming) }
    )
}

@DevicePreviews
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        bottomNav = {},
        state = HomeState(
            referenceAnimeTitle = "",
            similarAnimeTitle = "",
        ),
        onAction = {}
    )
}