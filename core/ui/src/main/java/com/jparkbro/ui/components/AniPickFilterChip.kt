package com.jparkbro.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickSecondary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.ChevronDownIcon

@Composable
fun APFilterTriggerChip(
    title: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(36.dp)
            .border(
                width = dimensionResource(R.dimen.border_width_default),
                color = if (isSelected) AniPickSecondary else AniPickGray50,
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable { onClick() }
            .padding(horizontal = dimensionResource(R.dimen.padding_default)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = AniPick16Normal.copy(
                color = if (isSelected) AniPickSecondary else AniPickBlack
            ),
        )
        Icon(
            imageVector = ChevronDownIcon,
            contentDescription = stringResource(R.string.chevron_down_icon),
            tint = if (isSelected) AniPickSecondary else AniPickGray100,
            modifier = Modifier
                .size(dimensionResource(R.dimen.icon_size_medium))
        )
    }
}

@Composable
fun APFilterOptionChip(
    title: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .border(
                width = dimensionResource(R.dimen.border_width_default),
                color = if (isSelected) AniPickSecondary else AniPickGray50,
                shape = AniPickSmallShape
            )
            .clip(AniPickSmallShape)
            .clickable { onClick() }
            .padding(horizontal = dimensionResource(R.dimen.padding_default)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = AniPick14Normal.copy(
                color = if (isSelected) AniPickSecondary else AniPickBlack
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun APFilterTriggerChipPreview() {
    APFilterTriggerChip(
        title = "년도",
        isSelected = true,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun APFilterOptionChipPreview() {
    APFilterOptionChip(
        title = "년도",
        isSelected = true,
        onClick = {}
    )
}