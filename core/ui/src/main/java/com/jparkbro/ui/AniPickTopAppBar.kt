package com.jparkbro.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.APColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APBaseTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APSimpleBackTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    handleBackNavigation: () -> Unit
) {
    APBaseTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { handleBackNavigation() },
                modifier = Modifier
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "",
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APLogoSearchTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateToSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.anipick_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(120.dp)
            )
            IconButton(
                onClick = { onNavigateToSearch() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_search_outline),
                    contentDescription = null,
                    tint = APColors.Gray,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = 1.dp,
            color = APColors.Gray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APTitledBackTopAppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    handleBackNavigation: () -> Unit
) {
    APBaseTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { handleBackNavigation() },
                modifier = Modifier
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "",
                )
            }
        },
        title = {
            Text(
                text = title,
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
fun APActionsBackTopAppBar(
    actions: @Composable RowScope.() -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    handleBackNavigation: () -> Unit
) {
    APBaseTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { handleBackNavigation() },
                modifier = Modifier
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "",
                )
            }
        },
        actions = actions,
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APSearchFieldBackTopAppBar(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    handleBackNavigation: () -> Unit,
    actions: @Composable () -> Unit,
) {
    APBaseTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { handleBackNavigation() },
                modifier = Modifier
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "",
                )
            }
        },
        title = {
            APBaseTextField(
                value = value,
                onValueChange = { onValueChange(it) },
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
                    .height(42.dp)
                    .padding(end = 16.dp),
                trailingIcon = { actions() }
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun APSimpleBackTopAppBarPreview() {
    APLogoSearchTopAppBar(
        onNavigateToSearch = {}
    )
}