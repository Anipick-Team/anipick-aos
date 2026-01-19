package com.jparkbro.mypage.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jparkbro.model.enum.UserContentType
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface

@Composable
internal fun RowScope.WatchStatusBox(
    title: String,
    count: Int,
    onAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(AniPickSurface, AniPickSmallShape)
            .height(80.dp)
            .widthIn(max = 120.dp)
            .weight(1f)
            .clip(AniPickSmallShape)
            .clickable { onAction() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small), Alignment.CenterVertically)
    ) {
        Text(
            text = title,
            style = AniPick14Normal.copy(color = AniPickBlack),
        )
        Text(
            text = stringResource(R.string.mypage_watch_status_count, count),
            style = AniPick12Normal.copy(color = AniPickPrimary)
        )
    }
}