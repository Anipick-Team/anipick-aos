package com.jparkbro.mypage

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.util.MimeTypeMap
import com.jparkbro.model.common.FormType
import com.jparkbro.model.mypage.ContentType
import com.jparkbro.model.mypage.LikedAnime
import com.jparkbro.model.mypage.LikedPerson
import com.jparkbro.ui.theme.APColors

@Composable
internal fun MyPage(
    bottomNav: @Composable () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateToUserContent: (ContentType) -> Unit,
    onNavigateToMyRatings: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onCheckSettingRefresh: () -> Boolean,
    onClearSettingRefresh: () -> Unit,
    onCheckStatusRefresh: () -> Boolean,
    onClearStatusRefresh: () -> Unit,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    // TODO 리렌더링 필요 ( 좋아요 , Watch Status , 닉네임 , 프로필 이미지 )


    val uiState by viewModel.uiState.collectAsState()
    val profileImage by viewModel.profileImage.collectAsState()

    MyPage(
        uiState = uiState,
        profileImage = profileImage,
        bottomNav = bottomNav,
        onChangeProfileImage = viewModel::editProfileImg,
        onGetInfo = viewModel::getInfo,
        onNavigateToAnimeDetail = onNavigateToAnimeDetail,
        onNavigateToUserContent = onNavigateToUserContent,
        onNavigateToMyRatings = onNavigateToMyRatings,
        onNavigateToSetting = onNavigateToSetting,
        onCheckSettingRefresh = onCheckSettingRefresh,
        onClearSettingRefresh = onClearSettingRefresh,
        onCheckStatusRefresh = onCheckStatusRefresh,
        onClearStatusRefresh = onClearStatusRefresh,
    )
}

@Composable
private fun MyPage(
    uiState: MyPageUiState = MyPageUiState.Loading,
    profileImage: String? = null,
    bottomNav: @Composable () -> Unit,
    onChangeProfileImage: (Uri) -> Unit,
    onGetInfo: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateToUserContent: (ContentType) -> Unit,
    onNavigateToMyRatings: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onCheckSettingRefresh: () -> Boolean,
    onClearSettingRefresh: () -> Unit,
    onCheckStatusRefresh: () -> Boolean,
    onClearStatusRefresh: () -> Unit,
) {

    LaunchedEffect(Unit) {
        val isSettingUpdate = onCheckSettingRefresh()
        val isStatusUpdate = onCheckStatusRefresh()

        if (isSettingUpdate || isStatusUpdate) {
            onGetInfo()

            if (isSettingUpdate) onClearSettingRefresh()
            if (isStatusUpdate) onClearStatusRefresh()
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "마이페이지",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    color = APColors.Black,
                )
                Box(
                    modifier = Modifier
                        .border(1.dp, APColors.Gray, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onNavigateToSetting() }
                        .padding(6.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_setting),
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp),
                        tint = APColors.Gray
                    )
                }
            }
        },
        bottomBar = { bottomNav() },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                is MyPageUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = APColors.Primary)
                    }
                }
                is MyPageUiState.Success -> {
                    val data = uiState.data

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ProfileImage(
                            profileImageUrl = profileImage,
                            onChangeProfileImage = onChangeProfileImage
                        )
                        Column {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = APColors.Primary)) {
                                        append(data.nickname)
                                    }
                                    withStyle(style = SpanStyle(color = APColors.Black)) {
                                        append(" 님, 애니픽과 함께")
                                    }
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W700,
                            )
                            Text(
                                text = "행복한 애니메이션 생활 즐기세요!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W700,
                                color = APColors.Black,
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .background(APColors.Surface, RoundedCornerShape(8.dp))
                                .size(width = 120.dp, height = 80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onNavigateToUserContent(ContentType.WATCHLIST) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "볼 애니",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${data.watchCounts.watchList}개",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Primary
                            )
                        }
                        Column(
                            modifier = Modifier
                                .background(APColors.Surface, RoundedCornerShape(8.dp))
                                .size(width = 120.dp, height = 80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onNavigateToUserContent(ContentType.WATCHING) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "보는 중",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${data.watchCounts.watching}개",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Primary
                            )
                        }
                        Column(
                            modifier = Modifier
                                .background(APColors.Surface, RoundedCornerShape(8.dp))
                                .size(width = 120.dp, height = 80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onNavigateToUserContent(ContentType.FINISHED) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "다 본 애니",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${data.watchCounts.finished}개",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W500,
                                color = APColors.Primary
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "평가한 작품",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W700,
                            color = APColors.Black
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_chevron_right),
                            contentDescription = null,
                            tint = APColors.TextGray,
                            modifier = Modifier
                                .clickable { onNavigateToMyRatings() }
                        )
                    }
                    LikedAnimes(
                        items = data.likedAnimes,
                        onNavigateToAnimeDetail = onNavigateToAnimeDetail,
                        onNavigateToUserContent = { onNavigateToUserContent(it) }
                    )
                    LikedPerson(
                        items = data.likedPersons,
                        onNavigateToUserContent = { onNavigateToUserContent(it) }
                    )
                }
                is MyPageUiState.Error -> {} // TODO
            }
        }
    }
}

@Composable
private fun ProfileImage(
    profileImageUrl: String? = null,
    onChangeProfileImage: (Uri) -> Unit,
) {
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { contentUri ->
        if (contentUri == null) {
            return@rememberLauncherForActivityResult
        }

        onChangeProfileImage(contentUri)
    }
    Box(
        modifier = Modifier
            .size(102.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { photoPicker.launch(
                PickVisualMediaRequest(
                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            ) }
    ) {
        if (profileImageUrl == null || profileImageUrl == "default.png") {
            Image(
                painter = painterResource(com.jparkbro.ui.R.drawable.profile_default_img),
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            AsyncImage(
                model = profileImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Icon(
            painter = painterResource(R.drawable.ic_edit),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .background(APColors.Primary, CircleShape)
                .padding(8.dp)
                .size(18.dp)
                .align(Alignment.BottomEnd)
        )
    }
}

@Composable
private fun LikedAnimes(
    items: List<LikedAnime> = emptyList(),
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateToUserContent: (ContentType) -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "좋아요한 작품",
                fontSize = 18.sp,
                fontWeight = FontWeight.W700,
                color = APColors.Black
            )
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = if (items.isEmpty()) APColors.LightGray else APColors.TextGray,
                modifier = Modifier
                    .clickable(
                        enabled = items.isNotEmpty()
                    ) { onNavigateToUserContent(ContentType.LIKED_ANIME) }
            )
        }
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(APColors.LightGray, RoundedCornerShape(8.dp))
                    .padding(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_mascot_empty),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .width(312.dp)
                )
                Text(
                    text = "아직 좋아요한 작품이 없어요.\n좋아요를 누르러 가볼까요?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black,
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { anime ->
                    Column(
                        modifier = Modifier
                            .width(116.dp)
                            .clickable { onNavigateToAnimeDetail(anime.animeId) },
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AsyncImage(
                            model = anime.coverImageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .size(width = 115.dp, height = 162.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "${anime.title}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LikedPerson(
    items: List<LikedPerson> = emptyList(),
    onNavigateToUserContent: (ContentType) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "좋아요한 인물",
                fontSize = 18.sp,
                fontWeight = FontWeight.W700,
                color = APColors.Black
            )
            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = if (items.isEmpty()) APColors.LightGray else APColors.TextGray,
                modifier = Modifier
                    .clickable(
                        enabled = items.isNotEmpty()
                    ) { onNavigateToUserContent(ContentType.LIKED_PERSON) }
            )
        }
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(APColors.LightGray, RoundedCornerShape(8.dp))
                    .padding(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_mascot_empty),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .width(312.dp)
                )
                Text(
                    text = "아직 좋아요한 인물이 없어요.\n좋아요를 누르러 가볼까요?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = APColors.Black,
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { person ->
                    Column(
                        modifier = Modifier
                            .width(116.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AsyncImage(
                            model = person.profileImageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .size(width = 115.dp, height = 105.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "${person.name}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 402)
@Composable
private fun ProfilePreview() {
    MyPage(
        uiState = MyPageUiState.Loading,
        bottomNav = {},
        onChangeProfileImage = {},
        onGetInfo = {},
        onNavigateToAnimeDetail = {},
        onNavigateToMyRatings = {},
        onNavigateToUserContent = {},
        onNavigateToSetting = {},
        onCheckSettingRefresh = { false },
        onClearSettingRefresh = {},
        onCheckStatusRefresh = { false },
        onClearStatusRefresh = {},
    )
}