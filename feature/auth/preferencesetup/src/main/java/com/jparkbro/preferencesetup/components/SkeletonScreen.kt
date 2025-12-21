package com.jparkbro.preferencesetup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jparkbro.preferencesetup.PreferenceSetupState
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APSkipActionTopAppBar
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ShimmerEffect


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SkeletonScreen(
    state: PreferenceSetupState,
) {
    Scaffold(
        topBar = { APSkipActionTopAppBar(onSkipClicked = {}) },
        modifier = Modifier
            .fillMaxSize(),
        containerColor = AniPickWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_40))
            ) {
                HeaderSkeleton()

                Column {
                    SearchSectionSkeleton(state = state)
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(vertical = dimensionResource(R.dimen.padding_large)),
                        thickness = dimensionResource(R.dimen.border_width_thick),
                        color = AniPickSurface
                    )
                    AnimeListSectionSkeleton()
                }
            }
            FooterSkeleton()
        }
    }
}

@Composable
private fun HeaderSkeleton() {
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.padding_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
    ) {
        Text(
            text = stringResource(R.string.preference_setup_title),
            style = AniPick20Bold.copy(color = Color.Transparent),
            modifier = Modifier
                .background(ShimmerEffect())
        )
        Text(
            text = stringResource(R.string.preference_setup_subtitle),
            style = AniPick14Normal.copy(color = Color.Transparent),
            modifier = Modifier
                .background(ShimmerEffect())
        )
    }
}


@Composable
private fun SearchSectionSkeleton(
    state: PreferenceSetupState,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.padding_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
    ) {
        Text(
            text = stringResource(R.string.preference_setup_rated_count, state.ratedAnimes.size),
            style = AniPick14Normal.copy(color = Color.Transparent),
            modifier = Modifier
                .background(ShimmerEffect())
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(AniPickSmallShape)
                .background(ShimmerEffect())
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(36.dp)
                    .clip(AniPickSmallShape)
                    .background(ShimmerEffect())
            )
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(36.dp)
                    .clip(AniPickSmallShape)
                    .background(ShimmerEffect())
            )
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(36.dp)
                    .clip(AniPickSmallShape)
                    .background(ShimmerEffect())
            )
        }
    }
}

@Composable
internal fun AnimeListSectionSkeleton() {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        repeat(10) {
            AnimeRatingCardSkeleton()
        }
    }
}

@Composable
private fun FooterSkeleton() {
    Column(
        modifier = Modifier
            .padding(bottom = dimensionResource(R.dimen.padding_extra_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_32))
    ) {
        HorizontalDivider(color = AniPickGray100)
        Box(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_large))
                .fillMaxWidth()
                .height(52.dp)
                .clip(AniPickSmallShape)
                .background(ShimmerEffect())
        )
    }
}