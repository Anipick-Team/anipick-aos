package com.jparkbro.mypage.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.jparkbro.model.common.UiState
import com.jparkbro.model.enum.UserContentType
import com.jparkbro.mypage.main.components.EmptyListItem
import com.jparkbro.mypage.main.components.NavigateTitle
import com.jparkbro.mypage.main.components.SkeletonScreen
import com.jparkbro.mypage.main.components.WatchStatusBox
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APActorCard
import com.jparkbro.ui.components.APAnimeCard
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APMyPageTopAppBar
import com.jparkbro.ui.theme.AniPick18ExtraBold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.EditIcon
import com.jparkbro.ui.util.rememberPhotoPickerWithPermission

@Composable
internal fun MyPageRoot(
    bottomNav: @Composable () -> Unit,
    onNavigateToUserContent: (UserContentType) -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    onNavigateToActor: (Long) -> Unit,
    viewModel: MyPageViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                bottomNav = bottomNav,
                onAction = viewModel::onAction
            )
        }
        UiState.Error -> {
            APErrorScreen(
                onClick = { viewModel.onAction(MyPageAction.OnRetryClicked) }
            )
        }
        UiState.Success -> {
            MyPageScreen(
                bottomNav = bottomNav,
                state = state,
                onAction = { action ->
                    when (action) {
                        MyPageAction.NavigateToSetting -> onNavigateToSetting()
                        is MyPageAction.NavigateToUserContent -> onNavigateToUserContent(action.contentType)
                        is MyPageAction.NavigateToInfoAnime -> onNavigateToInfoAnime(action.animeId)
                        is MyPageAction.NavigateToActor -> onNavigateToActor(action.actorId)
                    }
                    viewModel.onAction(action)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyPageScreen(
    bottomNav: @Composable () -> Unit,
    state: MyPageState,
    onAction: (MyPageAction) -> Unit
) {
    Scaffold(
        topBar = { APMyPageTopAppBar(onNavigateToSetting = { onAction(MyPageAction.NavigateToSetting) }) },
        bottomBar = bottomNav,
        containerColor = AniPickWhite
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            stickyHeader {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = dimensionResource(R.dimen.border_width_default),
                    color = AniPickSurface
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.padding_large), vertical = dimensionResource(R.dimen.padding_default)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large)),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ProfileImage(
                        state = state,
                        onAction = onAction
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = AniPickPrimary)) {
                                append(state.nickname)
                            }
                            withStyle(style = SpanStyle(color = AniPickBlack)) {
                                append(stringResource(R.string.mypage_welcome_title))
                            }
                        },
                        style = AniPick18ExtraBold
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.padding_large), vertical = dimensionResource(R.dimen.padding_default)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small))
                ) {
                    WatchStatusBox(
                        title = stringResource(R.string.mypage_watch_list_anime),
                        count = state.watchCounts?.watchList ?: 0,
                        onAction = { onAction(MyPageAction.NavigateToUserContent(UserContentType.WATCHLIST))}
                    )
                    WatchStatusBox(
                        title = stringResource(R.string.mypage_watching_anime),
                        count = state.watchCounts?.watching ?: 0,
                        onAction = { onAction(MyPageAction.NavigateToUserContent(UserContentType.WATCHING))}
                    )
                    WatchStatusBox(
                        title = stringResource(R.string.mypage_finished_anime),
                        count = state.watchCounts?.finished ?: 0,
                        onAction = { onAction(MyPageAction.NavigateToUserContent(UserContentType.FINISHED))}
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.padding_extra_large)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_32))
                ) {
                    NavigateTitle(
                        title = stringResource(R.string.mypage_rated_works),
                        onNavigateClick = { onAction(MyPageAction.NavigateToUserContent(UserContentType.RATING_REVIEW)) }
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
                    ) {
                        NavigateTitle(
                            title = stringResource(R.string.mypage_liked_anime),
                            onNavigateClick = { onAction(MyPageAction.NavigateToUserContent(UserContentType.LIKED_ANIME)) }
                        )
                        if (state.likeAnimes.isNotEmpty()) {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large)),
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                            ) {
                                items(state.likeAnimes) { anime ->
                                    APAnimeCard(
                                        cardWidth = 128.dp,
                                        imageUrl = anime.coverImageUrl,
                                        title = anime.title,
                                        maxLine = 2,
                                        onClick = { onAction(MyPageAction.NavigateToInfoAnime(anime.animeId ?: 0)) }
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_large))
                            ) {
                                EmptyListItem(contentText = stringResource(R.string.mypage_empty_liked_anime))
                            }
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
                    ) {
                        NavigateTitle(
                            title = stringResource(R.string.mypage_liked_actor),
                            onNavigateClick = { onAction(MyPageAction.NavigateToUserContent(UserContentType.LIKED_PERSON)) }
                        )
                        if (state.likeActors.isNotEmpty()) {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large)),
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                            ) {
                                items(state.likeActors) { actor ->
                                    APActorCard(
                                        cardWidth = 128.dp,
                                        imageUrl = actor.imageUrl,
                                        name = actor.name,
                                        maxLine = 2,
                                        onClick = { onAction(MyPageAction.NavigateToActor(actor.id ?: 0)) }
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_large))
                            ) {
                                EmptyListItem(contentText = stringResource(R.string.mypage_empty_liked_actor))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileImage(
    state: MyPageState,
    onAction: (MyPageAction) -> Unit,
) {
    // 권한 처리가 포함된 Photo Picker 사용
    val launchPhotoPicker = rememberPhotoPickerWithPermission(
        onImageSelected = { onAction(MyPageAction.OnChangeProfileImage(it)) }
    )

    Box(
        modifier = Modifier
            .size(102.dp)
            .clickable { launchPhotoPicker() }
    ) {
        AsyncImage(
            model = state.profileImageByteArray,
            contentDescription = stringResource(R.string.profile_img),
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = EditIcon,
            contentDescription = stringResource(R.string.edit_icon),
            tint = AniPickWhite,
            modifier = Modifier
                .background(AniPickPrimary, CircleShape)
                .padding(dimensionResource(R.dimen.padding_small))
                .size(dimensionResource(R.dimen.icon_size_small))
                .align(Alignment.BottomEnd)
        )
    }
}