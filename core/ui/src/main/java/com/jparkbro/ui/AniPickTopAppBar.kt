package com.jparkbro.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jparkbro.ui.theme.APColors

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
                    text = "건너뛰기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = Color(0xFFC9C9C9)
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
                contentDescription = "앱 로고",
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
                    contentDescription = "검색 아이콘",
                    tint = APColors.Gray
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
                        text = "무엇을 검색할까요?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = APColors.TextGray,
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
    title: Int,
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
                text = stringResource(title),
                fontSize = 18.sp,
                fontWeight = FontWeight.W700,
                color = APColors.Black
            )
        },
        scrollBehavior = scrollBehavior
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
        title = R.string.back_stack_icon,
        onNavigateBack = {},
    )
}