package com.jparkbro.review

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.model.common.FormType
import com.jparkbro.model.common.UiState
import com.jparkbro.review.components.BulletPointText
import com.jparkbro.review.components.SkeletonScreen
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APPrimaryActionButton
import com.jparkbro.ui.components.APSnackBarRe
import com.jparkbro.ui.components.APTitleTopAppBar
import com.jparkbro.ui.components.APToggleSwitch
import com.jparkbro.ui.components.updateRatingFromPosition
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.theme.AniPick12Normal
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick16Bold
import com.jparkbro.ui.theme.AniPick16Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickGray400
import com.jparkbro.ui.theme.AniPickPoint
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSecondary
import com.jparkbro.ui.theme.AniPickSmallShape
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.StarFillIcon
import com.jparkbro.ui.theme.StarOutlineIcon
import com.jparkbro.ui.util.ObserveAsEvents
import com.jparkbro.ui.util.extension.advancedImePadding

@Composable
internal fun ReviewFormRoot(
    onNavigateBack: () -> Unit,
    viewModel: ReviewFormViewModel = hiltViewModel()
) {
    var snackBarData by rememberSaveable { mutableStateOf<List<SnackBarData>>(emptyList()) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ReviewFormEvent.NavigateBack -> { onNavigateBack() }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                state = state,
                onAction = { action ->
                    when (action) {
                        ReviewFormAction.NavigateBack -> onNavigateBack()
                    }
                }
            )
        }

        UiState.Error -> {
            APErrorScreen(
                onClick = { viewModel.onAction(ReviewFormAction.OnRetryClicked) }
            )
        }

        UiState.Success -> {
            ReviewFormScreen(
                state = state,
                onAction = { action ->
                    when (action) {
                        ReviewFormAction.NavigateBack -> onNavigateBack()
                        ReviewFormAction.NavigateToGuideline -> {
                            val intent = Intent(Intent.ACTION_VIEW, "https://anipick.p-e.kr/community-guidelines.html".toUri())
                            context.startActivity(intent)
                        }
                    }
                    viewModel.onAction(action)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewFormScreen(
    state: ReviewFormState,
    onAction: (ReviewFormAction) -> Unit
) {
    val focusManager = LocalFocusManager.current

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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .advancedImePadding()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
        ) {
            stickyHeader {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = dimensionResource(R.dimen.border_width_default),
                    color = AniPickGray100
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensionResource(R.dimen.padding_large), vertical = dimensionResource(R.dimen.padding_extra_large)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_36))
                ) {
                    RatingContainer(
                        state = state,
                        onAction = onAction
                    )
                    ReviewContentSection(
                        state = state,
                        onAction = onAction,
                        focusManager = focusManager
                    )
                    APPrimaryActionButton(
                        text = stringResource(
                            when (state.formType) {
                                FormType.CREATE -> R.string.review_form_btn_create
                                FormType.EDIT -> R.string.review_form_btn_update
                            }
                        ),
                        onClick = { onAction(ReviewFormAction.OnSaveReviewClicked) },
                        enabled = state.isSaveEnabled
                    )
                }
            }
        }

    }
}

@Composable
private fun RatingContainer(
    state: ReviewFormState,
    onAction: (ReviewFormAction) -> Unit,
) {
    var rating by rememberSaveable { mutableFloatStateOf(state.animeReview?.rating ?: 0f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AniPickSurface, AniPickSmallShape)
            .padding(vertical = dimensionResource(R.dimen.spacing_extra_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
    ) {
        Row(
            modifier = Modifier
                .pointerInput(state.isAnimeRatingLoading) {
                    if (!state.isAnimeRatingLoading) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                // 드래그 시작 위치에서 별점 계산
                                updateRatingFromPosition(offset.x, size.width, 5).let {
                                    rating = it
                                }
                            },
                            onDragEnd = {
                                onAction(
                                    ReviewFormAction.OnRatingChanged(rating = rating)
                                )
                            },
                            onDragCancel = { rating = state.animeReview?.rating ?: 0f },
                            onDrag = { change, _ ->
                                // 드래그 위치에서 별점 계산
                                updateRatingFromPosition(change.position.x, size.width, 5).let {
                                    rating = it
                                }
                            }
                        )
                    }
                }
                .padding(vertical = dimensionResource(R.dimen.padding_small)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until 5) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.icon_size_extra_large))
                        .pointerInput(state.isAnimeRatingLoading) {
                            if (!state.isAnimeRatingLoading) {
                                detectTapGestures { offset ->
                                    val position = offset.x / size.width
                                    val newRating = if (position < 0.5f) {
                                        // 왼쪽 절반 (0.5점)
                                        i + 0.5f
                                    } else {
                                        // 오른쪽 절반 (1.0점)
                                        i + 1.0f
                                    }
                                    rating = newRating
                                    onAction(
                                        ReviewFormAction.OnRatingChanged(rating = rating)
                                    )
                                }
                            }
                        }
                ) {
                    val ratingInt = rating.toInt()
                    val hasHalfStar = rating.rem(1) != 0f

                    Image(
                        imageVector = StarOutlineIcon,
                        contentDescription = stringResource(R.string.outline_star_icon),
                        modifier = Modifier.fillMaxSize()
                    )
                    when {
                        i < ratingInt -> {
                            Image(
                                imageVector = StarFillIcon,
                                contentDescription = stringResource(R.string.fill_star_icon),
                                modifier = Modifier.fillMaxSize(),
                            )
                        }

                        i == ratingInt && hasHalfStar -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clipToBounds()
                                    .drawWithContent {
                                        // 왼쪽 반쪽만 클리핑
                                        clipRect(left = 0f, top = 0f, right = size.width / 2, bottom = size.height) {
                                            this@drawWithContent.drawContent()
                                        }
                                    }
                            ) {
                                Image(
                                    imageVector = StarFillIcon,
                                    contentDescription = stringResource(R.string.fill_star_icon),
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }
                    }
                }
            }
        }
        Text(
            text = stringResource(R.string.anime_detail_rating_format, rating),
            style = AniPick20Bold.copy(
                color = if (rating != 0f) AniPickPoint else AniPickGray100
            )
        )
    }
}

@Composable
private fun ReviewContentSection(
    state: ReviewFormState,
    onAction: (ReviewFormAction) -> Unit,
    focusManager: FocusManager
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.review_form_spoiler),
                    style = AniPick16Bold,
                    color = if (state.animeReview?.isSpoiler ?: false) AniPickSecondary else AniPickGray100
                )
                APToggleSwitch(
                    checked = state.animeReview?.isSpoiler ?: false,
                    checkedColor = AniPickSecondary,
                    unCheckedColor = AniPickGray100,
                    onCheckedChange = { onAction(ReviewFormAction.OnSpoilerClicked) },
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(AniPickSurface, AniPickSmallShape)
                .padding(dimensionResource(R.dimen.padding_large)),
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = state.content,
                textStyle = AniPick16Normal.copy(color = AniPickBlack),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                onKeyboardAction = KeyboardActionHandler {
                    focusManager.clearFocus()
                },
                cursorBrush = SolidColor(AniPickPrimary),
                decorator = { innerBox ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopStart
                    ) {
                        if (state.content.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.review_form_placeholder),
                                style = AniPick16Normal.copy(color = AniPickGray400),
                            )
                        }
                        innerBox()
                    }
                },
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.review_form_text_length, state.content.text.length),
                    style = AniPick14Normal.copy(
                        color = AniPickGray400
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
        ) {
            Text(
                text = stringResource(R.string.review_form_caution_title),
                style = AniPick16Normal.copy(color = AniPickGray400),
            )

            val guideTextStyle = AniPick12Normal.copy(color = AniPickGray400)
            Column {
                Text(
                    text = stringResource(R.string.review_form_caution_subtitle),
                    style = guideTextStyle
                )
                BulletPointText(
                    text = stringResource(R.string.review_form_caution_content_1),
                    textStyle = guideTextStyle
                )
                BulletPointText(
                    text = stringResource(R.string.review_form_caution_content_2),
                    textStyle = guideTextStyle
                )
                BulletPointText(
                    text = stringResource(R.string.review_form_caution_content_3),
                    textStyle = guideTextStyle
                )
                BulletPointText(
                    text = stringResource(R.string.review_form_caution_content_4),
                    textStyle = guideTextStyle
                )
                BulletPointText(
                    text = stringResource(R.string.review_form_caution_content_5),
                    textStyle = guideTextStyle
                )
            }
            Text(
                text = stringResource(R.string.review_form_btn_guideline),
                style = AniPick12Normal.copy(color = AniPickWhite),
                modifier = Modifier
                    .clip(AniPickSmallShape)
                    .clickable {
                        onAction(ReviewFormAction.NavigateToGuideline)
                    }
                    .background(AniPickGray400, AniPickSmallShape)
                    .padding(dimensionResource(R.dimen.padding_small))
            )
        }
    }
}