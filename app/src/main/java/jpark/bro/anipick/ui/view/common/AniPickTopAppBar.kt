package jpark.bro.anipick.ui.view.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jpark.bro.anipick.ui.theme.APColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APSimpleBackTopAppBar(
    handleBackNavigation: () -> Unit
) {
    TopAppBar(
        title = {},
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
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = APColors.Background
        )
    )
}

@Preview(showBackground = true)
@Composable
fun APSimpleBackTopAppBarPreview() {
    APSimpleBackTopAppBar {}
}