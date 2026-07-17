package com.jparkbro.mypage.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.ui.R
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick18Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickExtraSmallShape
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickWhite

@Composable
internal fun FeedbackLinkCard(
    modifier: Modifier = Modifier,
    onFeedbackClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .widthIn(max = 400.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF186D97), Color(0xFF16A1A8), Color(0xFF5CC398)),
                    start = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                    end = Offset(0f, 0f),
                ),
                shape = AniPickSmallShape
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(start = dimensionResource(R.dimen.padding_extra_large))
                .padding(vertical = dimensionResource(R.dimen.padding_extra_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large))
        ) {
            Column {
                Text(
                    text = stringResource(R.string.mypage_feedback_title),
                    style = AniPick18Bold.copy(color = AniPickWhite)
                )
                Text(
                    text = stringResource(R.string.mypage_feedback_description),
                    style = AniPick14Normal.copy(color = AniPickWhite)
                )
            }
            TextButton(
                onClick = onFeedbackClick,
                shape = AniPickExtraSmallShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AniPickWhite,
                    contentColor = AniPickBlack
                ),
                contentPadding = PaddingValues(vertical = dimensionResource(R.dimen.padding_small)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.mypage_feedback_button),
                    style = AniPick14Normal,
                )
            }
        }
        Image(
            painter = painterResource(R.drawable.feedback_card_cha),
            contentDescription = "피드백 마스코트 이미지",
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentHeight(Alignment.Bottom)
                .padding(
                    start = dimensionResource(R.dimen.padding_extra_large),
                    end = dimensionResource(R.dimen.padding_medium)
                )
                .width(100.dp)
        )
    }
}

@DevicePreviews
@Composable
fun FeedbackLinkCardPreview() {
    FeedbackLinkCard(
        onFeedbackClick = {}
    )
}
