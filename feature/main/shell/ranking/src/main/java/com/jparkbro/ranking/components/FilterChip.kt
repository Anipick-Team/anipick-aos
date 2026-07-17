package com.jparkbro.ranking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickWhite

@Composable
internal fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        style = AniPick16Normal.copy(color = AniPickWhite),
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .background(if (isSelected) AniPickPrimary else AniPickGray100, CircleShape)
            .padding(horizontal = dimensionResource(R.dimen.padding_medium), vertical = dimensionResource(R.dimen.padding_small))
    )
}