package com.jparkbro.mypage.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jparkbro.model.enum.UserContentType
import com.jparkbro.mypage.detail.UserContentAction
import com.jparkbro.mypage.detail.UserContentState
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APTitleTopAppBar
import com.jparkbro.ui.components.AnimeSkeleton
import com.jparkbro.ui.components.ReviewSkeleton
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.util.rememberGridInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SkeletonScreen(
    state: UserContentState,
    onAction: (UserContentAction) -> Unit
) {
    val horizontalPadding = dimensionResource(R.dimen.padding_large)
    val spacing = 8.dp

    Scaffold(
        topBar = {
            APTitleTopAppBar(
                title = when (state.contentType) {
                    UserContentType.WATCHLIST -> stringResource(R.string.user_content_header_watch_list_anime)
                    UserContentType.WATCHING -> stringResource(R.string.user_content_header_watching_anime)
                    UserContentType.FINISHED -> stringResource(R.string.user_content_header_finished_anime)
                    UserContentType.LIKED_ANIME -> stringResource(R.string.user_content_header_liked_anime)
                    UserContentType.LIKED_PERSON -> stringResource(R.string.user_content_header_liked_actor)
                    UserContentType.RATING_REVIEW -> stringResource(R.string.user_content_header_rated_works)
                    else -> ""
                },
                onNavigateBack = { onAction(UserContentAction.NavigateBack) },
            )
        },
        containerColor = AniPickSurface
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
            when (state.contentType) {
                UserContentType.RATING_REVIEW -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = dimensionResource(R.dimen.padding_large),
                            end = dimensionResource(R.dimen.padding_large),
                            top = dimensionResource(R.dimen.spacing_medium),
                        ),
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium)),
                        userScrollEnabled = false
                    ) {
                        items(5) {
                            ReviewSkeleton()
                        }
                    }
                }

                else -> {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AniPickWhite)
                    ) {

                        val gridInfo = rememberGridInfo(
                            availableWidth = maxWidth,
                            horizontalPadding = horizontalPadding * 2,
                            spacing = spacing,
                            defaultItemWidth = 128.dp,
                            minColumns = 3,
                            maxColumns = 5
                        )

                        LazyVerticalGrid(
                            userScrollEnabled = false,
                            columns = GridCells.Fixed(gridInfo.columns),
                            horizontalArrangement = Arrangement.spacedBy(spacing),
                            verticalArrangement = Arrangement.spacedBy(
                                dimensionResource(R.dimen.spacing_extra_large)
                            ),
                            contentPadding = PaddingValues(
                                start = horizontalPadding,
                                end = horizontalPadding,
                                top = dimensionResource(R.dimen.spacing_extra_large)
                            )
                        ) {
                            items(40) {
                                AnimeSkeleton(width = gridInfo.itemWidth)
                            }
                        }
                    }
                }
            }
        }
    }
}