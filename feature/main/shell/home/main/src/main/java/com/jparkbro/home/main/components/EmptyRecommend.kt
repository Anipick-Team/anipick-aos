package com.jparkbro.home.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.jparkbro.ui.R
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.RecommendNullImg

@Composable
internal fun EmptyRecommend() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AniPickWhite)
            .padding(vertical = dimensionResource(R.dimen.padding_huge)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            imageVector = RecommendNullImg,
            contentDescription = stringResource(R.string.empty_recommend_img)
        )
    }
}