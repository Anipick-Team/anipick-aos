package com.jparkbro.mypage.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.jparkbro.model.mypage.ContentType
import com.jparkbro.mypage.main.MyPageAction
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APMyPageTopAppBar
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick18ExtraBold
import com.jparkbro.ui.theme.AniPick18Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ShimmerEffect

@Composable
internal fun SkeletonScreen(
    bottomNav: @Composable () -> Unit,
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
            userScrollEnabled = false
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensionResource(R.dimen.padding_large), vertical = dimensionResource(R.dimen.spacing_extra_large)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_32))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .background(ShimmerEffect(), CircleShape)
                        )
                        Text(
                            text = "동당동당 님, 애니픽과 함께\n행복한 애니메이션 생활 즐기세요!",
                            style = AniPick18ExtraBold,
                            color = Color.Transparent,
                            modifier = Modifier
                                .background(ShimmerEffect())
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small))
                    ) {
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .background(ShimmerEffect(), AniPickSmallShape)
                                    .height(80.dp)
                                    .widthIn(max = 120.dp)
                                    .weight(1f)
                            )
                        }
                    }
                    repeat(3) {
                        Column(
                            modifier = Modifier
                                .background(AniPickWhite),
                            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.home_main_upcoming_release),
                                    style = AniPick20Bold.copy(color = Color.Transparent),
                                    modifier = Modifier
                                        .background(brush = ShimmerEffect())
                                )
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(brush = ShimmerEffect())
                                )
                            }
                            LazyRow(
                                userScrollEnabled = false,
                                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                            ) {
                                items(10) {
                                    Column(
                                        modifier = Modifier
                                            .width(128.dp),
                                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(128.dp)
                                                .aspectRatio(2f/3f)
                                                .clip(AniPickSmallShape)
                                                .background(brush = ShimmerEffect())
                                        )
                                        Text(
                                            text = "철권: 블러드라인",
                                            style = AniPick16Normal.copy(color = Color.Transparent),
                                            modifier = Modifier
                                                .background(brush = ShimmerEffect())
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}