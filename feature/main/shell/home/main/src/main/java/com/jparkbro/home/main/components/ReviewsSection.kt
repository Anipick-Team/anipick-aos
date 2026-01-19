package com.jparkbro.home.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.ChevronRightIcon

@Composable
internal fun ReviewsSection(
    itemList: @Composable () -> Unit,
    onNavigateClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .padding(vertical = dimensionResource(R.dimen.padding_huge)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_large))
                .clickable { onNavigateClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.home_main_latest_review),
                style = AniPick20Bold.copy(color = AniPickBlack)
            )
            Icon(
                imageVector = ChevronRightIcon,
                contentDescription = stringResource(R.string.chevron_right_icon),
                tint = AniPickGray100,
            )
        }
        itemList()
    }
}