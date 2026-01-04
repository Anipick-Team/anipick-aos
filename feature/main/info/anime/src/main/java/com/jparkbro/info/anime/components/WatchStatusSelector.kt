package com.jparkbro.info.anime.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickWhite

@Composable
internal fun RowScope.WatchStatusSelector(
    text: String,
    status: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = AniPick14Normal.copy(
            color = if (status) AniPickWhite else AniPickBlack
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .weight(1f)
            .clip(AniPickSmallShape)
            .clickable(enabled) { onClick() }
            .background(
                color = if (status) AniPickPrimary else AniPickGray50,
                shape = AniPickSmallShape
            )
            .padding(vertical = dimensionResource(R.dimen.padding_small))
    )
}
