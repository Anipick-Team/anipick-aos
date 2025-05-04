package jpark.bro.ui.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jpark.bro.ui.R
import jpark.bro.ui.theme.APColors
import kotlin.math.sin

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
            containerColor = APColors.Background
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
    APBaseTopAppBar(
        navigationIcon = {
            Image(
                painter = painterResource(R.drawable.anipick_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(120.dp)
            )
        },
        actions = {
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
        },
        scrollBehavior = scrollBehavior,
        modifier = Modifier
            .border(1.dp, APColors.Gray)
    )
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
fun APSearchFieldBackTopAppBar(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
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
            APBaseTextField(
                value = value,
                onValueChange = { onValueChange(it) },
                placeholder = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search_outline),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(16.dp),
                            tint = APColors.TextGray
                        )
                        Text(
                            text = "무엇을 검색할까요?",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = APColors.TextGray,
                        )
                    }
                },
                keyboardActions = keyboardActions,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .padding(end = 16.dp),
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, heightDp = 100)
@Composable
fun APSimpleBackTopAppBarPreview() {
    APLogoSearchTopAppBar(
        onNavigateToSearch = {}
    )
}