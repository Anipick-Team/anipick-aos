package com.jparkbro.home.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jparkbro.home.detail.HomeDetailAction
import com.jparkbro.home.detail.HomeDetailState
import com.jparkbro.model.enum.HomeDetailSortType
import com.jparkbro.model.home.Sort
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ChevronDownIcon
import com.jparkbro.ui.theme.ChevronUpIcon

@Composable
internal fun DropdownButton(
    state: HomeDetailState,
    onAction: (HomeDetailAction) -> Unit
) {
    var showDropdown by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Row(
                modifier = Modifier
                    .clickable {
                        showDropdown = !showDropdown
                    }
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
                    text = HomeDetailSortType.LATEST.displayName,
                    style = AniPick14Normal.copy(
                        color = if (state.sort == HomeDetailSortType.LATEST) AniPickBlack else AniPickGray400
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDropdown = !showDropdown
                            onAction(HomeDetailAction.OnSortClicked(HomeDetailSortType.LATEST))
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
                    text = HomeDetailSortType.POPULARITY.displayName,
                    style = AniPick14Normal.copy(
                        color = if (state.sort == HomeDetailSortType.POPULARITY) AniPickBlack else AniPickGray400
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDropdown = !showDropdown
                            onAction(HomeDetailAction.OnSortClicked(HomeDetailSortType.POPULARITY))
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
                    text = HomeDetailSortType.START_DATE.displayName,
                    style = AniPick14Normal.copy(
                        color = if (state.sort == HomeDetailSortType.START_DATE) AniPickBlack else AniPickGray400
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDropdown = !showDropdown
                            onAction(HomeDetailAction.OnSortClicked(HomeDetailSortType.START_DATE))
                        }
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}