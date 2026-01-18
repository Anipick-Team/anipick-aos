package com.jparkbro.setting.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray50
import com.jparkbro.ui.theme.AniPickGray500
import com.jparkbro.ui.theme.ChevronRightIcon

@Composable
internal fun NavigateItem(
    title: String,
    titleColor: Color = AniPickBlack,
    content: @Composable () -> Unit = {},
    isEnabled: Boolean = true,
    isIcon: Boolean = true,
    onNavigate: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = isEnabled
            ) {
                onNavigate?.invoke()
            },
        horizontalArrangement = Arrangement.SpaceBetween ,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = title,
            style = AniPick16Normal.copy(color = titleColor)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
            if (isIcon) {
                Icon(
                    imageVector = ChevronRightIcon,
                    contentDescription = stringResource(R.string.chevron_right_icon),
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_medium)),
                    tint = if (isEnabled) AniPickGray500 else AniPickGray50
                )
            }
        }
    }
}