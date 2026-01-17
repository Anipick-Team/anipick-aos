package com.jparkbro.mypage.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.jparkbro.model.enum.ReviewSortType
import com.jparkbro.mypage.detail.UserContentAction
import com.jparkbro.mypage.detail.UserContentState
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ChevronDownIcon
import com.jparkbro.ui.theme.ChevronUpIcon

@Composable
internal fun SortDropdownBtn(
    state: UserContentState,
    onAction: (UserContentAction) -> Unit
) {
    var showDropdown by rememberSaveable { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .clickable { showDropdown = !showDropdown }
                .padding(vertical = dimensionResource(R.dimen.padding_extra_small)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.reviewSort.displayName,
                style = AniPick14Normal.copy(color = AniPickGray400)
            )
            Icon(
                imageVector = if (showDropdown) ChevronUpIcon else ChevronDownIcon,
                contentDescription = stringResource(R.string.chevron_up_down_icon),
                tint = AniPickGray400,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.icon_size_small))
            )
        }
        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false },
            offset = DpOffset(x = 0.dp, y = 8.dp),
            shape = AniPickSmallShape,
            containerColor = AniPickWhite,
            shadowElevation = 2.dp,
        ) {
            Text(
                text = ReviewSortType.LATEST.displayName,
                style = AniPick14Normal.copy(
                    color = if (state.reviewSort == ReviewSortType.LATEST) AniPickBlack else AniPickGray400
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDropdown = !showDropdown
                        onAction(UserContentAction.OnChangeSortType(ReviewSortType.LATEST))
                    }
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_default)),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickSurface
            )
            Text(
                text = ReviewSortType.LIKES.displayName,
                style = AniPick14Normal.copy(
                    color = if (state.reviewSort == ReviewSortType.LIKES) AniPickBlack else AniPickGray400
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDropdown = !showDropdown
                        onAction(UserContentAction.OnChangeSortType(ReviewSortType.LIKES))
                    }
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_default)),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickSurface
            )
            Text(
                text = ReviewSortType.RATING_DESC.displayName,
                style = AniPick14Normal.copy(
                    color = if (state.reviewSort == ReviewSortType.RATING_DESC) AniPickBlack else AniPickGray400
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDropdown = !showDropdown
                        onAction(UserContentAction.OnChangeSortType(ReviewSortType.RATING_DESC))
                    }
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_default)),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickSurface
            )
            Text(
                text = ReviewSortType.RATING_ASC.displayName,
                style = AniPick14Normal.copy(
                    color = if (state.reviewSort == ReviewSortType.RATING_ASC) AniPickBlack else AniPickGray400
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDropdown = !showDropdown
                        onAction(UserContentAction.OnChangeSortType(ReviewSortType.RATING_ASC))
                    }
                    .padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}