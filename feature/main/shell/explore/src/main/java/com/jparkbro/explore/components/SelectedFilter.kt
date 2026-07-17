package com.jparkbro.explore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.CloseIcon

@Composable
internal fun SelectedFilter(
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .background(AniPickPrimary.copy(alpha = 0.2f), CircleShape)
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(horizontal = dimensionResource(R.dimen.padding_medium), vertical = dimensionResource(R.dimen.padding_small)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = AniPick14Normal.copy(color = AniPickPrimary)
        )
        Icon(
            imageVector = CloseIcon,
            contentDescription = stringResource(R.string.close_icon),
            modifier = Modifier
                .size(dimensionResource(R.dimen.icon_size_small)),
            tint = AniPickPrimary
        )
    }
}