package com.jparkbro.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick18ExtraBold
import com.jparkbro.ui.theme.AniPick24Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray200
import com.jparkbro.ui.theme.AniPickGray300
import com.jparkbro.ui.theme.AniPickSmallShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APBaseTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(containerColor = APColors.White)
) {
    CenterAlignedTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = colors
    )
}

/** BackStack Navigation TopAppBar */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APBackStackTopAppBar(
    onNavigateBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    APBaseTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_left),
                    contentDescription = stringResource(R.string.back_stack_icon),
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

/** Skip Action TopAppBar */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APSkipActionTopAppBar(
    onClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    APBaseTopAppBar(
        actions = {
            TextButton(
                onClick = onClick
            ) {
                Text(
                    text = stringResource(R.string.skip),
                    style = AniPick16Normal.copy(color = AniPickGray200),
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

/** Navigation Logo, Search Action Icon */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APMainTopAppBar(
    onNavigateToSearch: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    APBaseTopAppBar(
        navigationIcon = {
            Image(
                painter = painterResource(R.drawable.anipick_logo),
                contentDescription = stringResource(R.string.app_logo),
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.padding_default))
                    .width(120.dp)
            )
        },
        actions = {
            IconButton(
                onClick = onNavigateToSearch
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search_outline),
                    contentDescription = stringResource(R.string.search_icon),
                    tint = AniPickGray100,
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_large))
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

/** BackStack Icon, Search TextField */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APSearchTopAppBar(
    onNavigateBack: () -> Unit,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    APBaseTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_left),
                    contentDescription = stringResource(R.string.back_stack_icon),
                )
            }
        },
        title = {
            APBaseTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_placeholder),
                        style = AniPick16Normal.copy(color = AniPickGray300),
                    )
                },
                keyboardActions = keyboardActions,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(end = dimensionResource(R.dimen.padding_extra_small)),
                trailingIcon = trailingIcon
            )
        },
        scrollBehavior = scrollBehavior
    )
}

/** BackStack Icon, Title */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APTitleTopAppBar(
    title: String,
    onNavigateBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    APBaseTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_left),
                    contentDescription = stringResource(R.string.back_stack_icon),
                )
            }
        },
        title = {
            Text(
                text = title,
                style = AniPick18ExtraBold.copy(color = AniPickBlack),
            )
        },
        scrollBehavior = scrollBehavior
    )
}

/** MyPage TopAppBar */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APMyPageTopAppBar(
    onNavigateToSetting: () -> Unit = {},
) {
    APBaseTopAppBar(
        navigationIcon = {
            Row(
                modifier = Modifier
                .padding(start = dimensionResource(R.dimen.padding_default)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.my_page),
                    style = AniPick24Bold.copy(color = AniPickBlack),
                )
                Box(
                    modifier = Modifier
                        .border(dimensionResource(R.dimen.border_width_default), APColors.Gray, AniPickSmallShape)
                        .clip(AniPickSmallShape)
                        .clickable { onNavigateToSetting() }
                        .padding(dimensionResource(R.dimen.padding_small))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_setting),
                        contentDescription = stringResource(R.string.setting_icon),
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_small)),
                        tint = AniPickGray100
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun APBackStackTopAppBarPreview() {
    APBackStackTopAppBar(
        onNavigateBack = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun APSkipActionTopAppBarPreview() {
    APSkipActionTopAppBar(
        onClick = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun APMainTopAppBarPreview() {
    APMainTopAppBar(
        onNavigateToSearch = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun APSearchTopAppBarPreview() {
    APSearchTopAppBar(
        onNavigateBack = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun APTitleTopAppBarPreview() {
    APTitleTopAppBar(
        title = stringResource(R.string.back_stack_icon),
        onNavigateBack = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
private fun APMyPageTopAppBarPreview() {
    APMyPageTopAppBar(

    )
}