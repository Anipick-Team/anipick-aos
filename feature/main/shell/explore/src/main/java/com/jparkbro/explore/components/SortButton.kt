package com.jparkbro.explore.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.jparkbro.explore.ExploreAction
import com.jparkbro.explore.ExploreState
import com.jparkbro.model.enum.ExploreSortType
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
internal fun SortButton(
    state: ExploreState,
    onAction: (ExploreAction) -> Unit
) {
    var showDropdown by rememberSaveable { mutableStateOf(false) }

    Box{
        Row(
            modifier = Modifier
                .clickable { showDropdown = !showDropdown }
                .padding(vertical = dimensionResource(R.dimen.padding_extra_small)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.sort.displayName,
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
                text = ExploreSortType.POPULARITY.displayName,
                style = AniPick14Normal.copy(
                    color = if (state.sort == ExploreSortType.POPULARITY) AniPickBlack else AniPickGray400
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDropdown = !showDropdown
                        onAction(ExploreAction.OnSortChanged(ExploreSortType.POPULARITY))
                    }
                    .padding(
                        horizontal = dimensionResource(R.dimen.padding_huge),
                        vertical = dimensionResource(R.dimen.padding_medium)
                    )
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.padding_default), vertical = dimensionResource(R.dimen.padding_extra_small)),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickSurface
            )
            Text(
                text = ExploreSortType.RATING.displayName,
                style = AniPick14Normal.copy(
                    color = if (state.sort == ExploreSortType.RATING) AniPickBlack else AniPickGray400
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDropdown = !showDropdown
                        onAction(ExploreAction.OnSortChanged(ExploreSortType.RATING))
                    }
                    .padding(
                        horizontal = dimensionResource(R.dimen.padding_huge),
                        vertical = dimensionResource(R.dimen.padding_medium)
                    )
            )
        }
    }
}