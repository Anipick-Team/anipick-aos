package com.jparkbro.home.impl.detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.core.designsystem.R
import com.jparkbro.core.designsystem.icon.Notice
import com.jparkbro.core.designsystem.theme.AniPickTheme
import com.jparkbro.core.ui.util.DevicePreviews
import com.jparkbro.home.api.HomeDetailType
import com.jparkbro.home.impl.components.DayOfWeekSelector
import com.jparkbro.home.impl.detail.HomeDetailAction
import com.jparkbro.home.impl.detail.HomeDetailState
import com.jparkbro.home.impl.main.components.RecommendationSectionTitle
import com.jparkbro.home.impl.main.components.SimilarRecommendationSectionTitle

/** 타입별로 그리드 위에 얹는 헤더 */
@Composable
internal fun DetailHeader(
    state: HomeDetailState,
    onAction: (HomeDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (val type = state.type) {
        is HomeDetailType.Recommendation -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(AniPickTheme.colors.black, RoundedCornerShape(8.dp))
            ) {
                if (type.basedOnAnimeId == null) {
                    RecommendationSectionTitle(
                        nickname = state.nickname,
                        referenceAnimeTitle = state.referenceAnimeTitle,
                        titleStyle = AniPickTheme.typography.h2,
                        accentColor = AniPickTheme.colors.white,
                        baseColor = AniPickTheme.colors.white,
                        modifier = Modifier.padding(20.dp),
                    )
                } else {
                    SimilarRecommendationSectionTitle(
                        referenceAnimeTitle = state.referenceAnimeTitle,
                        titleStyle = AniPickTheme.typography.h2,
                        accentColor = AniPickTheme.colors.white,
                        baseColor = AniPickTheme.colors.white,
                        modifier = Modifier.padding(20.dp),
                    )
                }
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.home_banner_mascot),
                    contentDescription = "배너 마스코트 이미지",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(top = 76.dp, end = 24.dp)
                )
            }
        }
        HomeDetailType.Weekly -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                WeeklyScheduleNotice()
                DayOfWeekSelector(
                    selectedDay = state.selectedDayOfWeek,
                    onDaySelected = { day -> onAction(HomeDetailAction.OnDaySelected(day)) },
                )
            }
        }
        HomeDetailType.ComingSoon -> {
            ComingSoonSortHeader(
                selectedSort = state.sort,
                onSortSelected = { sort -> onAction(HomeDetailAction.OnSortSelected(sort)) },
                modifier = modifier.fillMaxWidth(),
            )
        }
    }
}

/** 요일별 신작 방영 일정 기준 안내 */
@Composable
private fun WeeklyScheduleNotice(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(AniPickTheme.colors.gray, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            imageVector = Notice,
            contentDescription = null,
        )
        Text(
            text = "제공되는 정보는 공식 방영 일정을 기준으로 하며,\nOTT 플랫폼별 방영 일정과는 다를 수 있습니다.",
            style = AniPickTheme.typography.caption2,
            color = AniPickTheme.colors.black,
        )
    }
}

@DevicePreviews
@Composable
private fun DetailHeaderRecommendationPreview() {
    DetailHeader(
        state = HomeDetailState(type = HomeDetailType.Recommendation(), nickname = "닉네임"),
        onAction = {},
    )
}

@DevicePreviews
@Composable
private fun DetailHeaderWeeklyPreview() {
    DetailHeader(
        state = HomeDetailState(type = HomeDetailType.Weekly),
        onAction = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailHeaderComingSoonPreview() {
    DetailHeader(
        state = HomeDetailState(type = HomeDetailType.ComingSoon, sort = "popularity"),
        onAction = {},
    )
}
