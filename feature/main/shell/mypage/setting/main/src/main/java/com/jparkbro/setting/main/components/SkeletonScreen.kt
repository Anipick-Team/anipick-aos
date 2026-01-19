package com.jparkbro.setting.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APTitleTopAppBar
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ShimmerEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SkeletonScreen(
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            APTitleTopAppBar(
                title = stringResource(R.string.setting_header),
                onNavigateBack = onNavigateBack,
            )
        },
        containerColor = AniPickSurface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickSurface
            )
            LazyColumn(
                userScrollEnabled = false,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
            ) {
                items(3) {
                    SkeletonSection()
                }
            }
        }
    }
}

@Composable
private fun SkeletonSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AniPickWhite)
            .padding(horizontal = dimensionResource(R.dimen.padding_large), vertical = dimensionResource(R.dimen.padding_huge)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_36))
    ) {
        Text(
            text = stringResource(R.string.setting_category_account),
            style = AniPick20Bold.copy(color = Color.Transparent),
            modifier = Modifier
                .background(ShimmerEffect())
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large))
        ) {
            repeat(4) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.setting_item_privacy_policy),
                        style = AniPick16Normal.copy(color = Color.Transparent),
                        modifier = Modifier
                            .background(ShimmerEffect())
                    )
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_medium))
                            .background(ShimmerEffect())
                    )
                }
            }
        }
    }
}