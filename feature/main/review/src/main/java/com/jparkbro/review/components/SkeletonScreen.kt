package com.jparkbro.review.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jparkbro.model.common.FormType
import com.jparkbro.review.ReviewFormAction
import com.jparkbro.review.ReviewFormState
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APPrimaryActionButton
import com.jparkbro.ui.components.APTitleTopAppBar
import com.jparkbro.ui.components.APToggleSwitch
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick16Bold
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.ShimmerEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SkeletonScreen(
    state: ReviewFormState,
    onAction: (ReviewFormAction) -> Unit
) {
    Scaffold(
        topBar = {
            APTitleTopAppBar(
                title = stringResource(
                    when (state.formType) {
                        FormType.CREATE -> R.string.review_form_header_create
                        FormType.EDIT -> R.string.review_form_header_update
                    }
                ),
                onNavigateBack = { onAction(ReviewFormAction.NavigateBack) },
            )
        },
        containerColor = AniPickWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = dimensionResource(R.dimen.border_width_default),
                color = AniPickGray100
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = dimensionResource(R.dimen.padding_extra_large)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_36))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.padding_large))
                        .height(124.dp)
                        .background(ShimmerEffect(), AniPickSmallShape)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.padding_large))
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
                        ) {
                            Text(
                                text = "스포일러",
                                style = AniPick16Bold.copy(color = Color.Transparent),
                                modifier = Modifier
                                    .background(ShimmerEffect())
                            )
                            Text(
                                text = "스포 ",
                                style = AniPick16Bold.copy(color = Color.Transparent),
                                modifier = Modifier
                                    .background(ShimmerEffect())
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .background(ShimmerEffect(), AniPickSmallShape)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .background(ShimmerEffect(), AniPickSmallShape)
                    )
                }
                APPrimaryActionButton(
                    text = stringResource(
                        when (state.formType) {
                            FormType.CREATE -> R.string.review_form_btn_create
                            FormType.EDIT -> R.string.review_form_btn_update
                        }
                    ),
                    onClick = { },
                    enabled = false,
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_large))
                )
            }
        }
    }
}