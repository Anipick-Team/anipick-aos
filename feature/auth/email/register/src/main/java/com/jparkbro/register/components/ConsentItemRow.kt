package com.jparkbro.register.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray300
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.CheckIcon
import com.jparkbro.ui.theme.ChevronRightIcon

@Composable
internal fun ConsentItemRow(
    text: String,
    isChecked: Boolean = false,
    onCheckedChange: () -> Unit = {},
    onNavigateToTerms: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable { onCheckedChange() },
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = CheckIcon,
                contentDescription = stringResource(R.string.check_icon),
                tint = if (isChecked) AniPickPrimary else AniPickGray400,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_small))
            )
            Text(
                text = text,
                style = AniPick14Normal.copy(color = AniPickBlack),
            )
        }
        onNavigateToTerms?.let {
            Icon(
                imageVector = ChevronRightIcon,
                contentDescription = stringResource(R.string.chevron_right_icon),
                tint = AniPickGray300,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { it() }
            )
        }
    }
}

@DevicePreviews
@Composable
private fun ConsentItemRowPreview() {
    ConsentItemRow(
        text = "[필수] 만 14세 이상입니다.",
        onNavigateToTerms = {}
    )
}