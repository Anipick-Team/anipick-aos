package com.jparkbro.preferencesetup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.model.common.MetaData
import com.jparkbro.model.common.UiState
import com.jparkbro.model.enum.BottomSheetType
import com.jparkbro.preferencesetup.components.AnimeListSectionSkeleton
import com.jparkbro.preferencesetup.components.AnimeRatingCard
import com.jparkbro.preferencesetup.components.SkeletonScreen
import com.jparkbro.ui.R
import com.jparkbro.ui.components.APBaseTextField
import com.jparkbro.ui.components.APEmptyContent
import com.jparkbro.ui.components.APErrorScreen
import com.jparkbro.ui.components.APFilterBottomSheet
import com.jparkbro.ui.components.APFilterTriggerChip
import com.jparkbro.ui.components.APPrimaryActionButton
import com.jparkbro.ui.components.APSkipActionTopAppBar
import com.jparkbro.ui.model.BottomSheetData
import com.jparkbro.ui.preview.DevicePreviews
import com.jparkbro.ui.theme.APColors
import com.jparkbro.ui.theme.AniPick14Normal
import com.jparkbro.ui.theme.AniPick20Bold
import com.jparkbro.ui.theme.AniPickBlack
import com.jparkbro.ui.theme.AniPickGray100
import com.jparkbro.ui.theme.AniPickPoint
import com.jparkbro.ui.theme.AniPickPrimary
import com.jparkbro.ui.theme.AniPickSecondary
import com.jparkbro.ui.theme.AniPickSurface
import com.jparkbro.ui.theme.AniPickWhite
import com.jparkbro.ui.theme.CloseIcon
import com.jparkbro.ui.theme.SearchOutlineIcon
import com.jparkbro.ui.util.ObserveAsEvents
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
internal fun PreferenceSetupRoot(
    metaData: MetaData,
    onNavigateToHome: () -> Unit,
    viewModel: PreferenceSetupViewModel = hiltViewModel()
) {
    var bottomSheetData by rememberSaveable { mutableStateOf<BottomSheetData?>(null) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is PreferenceSetupEvent.ShowBottomSheet -> {
                bottomSheetData = event.data.copy(
                    onDismiss = { bottomSheetData = null },
                    onConfirm = { viewModel.onAction(PreferenceSetupAction.OnFilterCompleteClicked(it)) }
                )
            }

            PreferenceSetupEvent.SubmitSuccess -> onNavigateToHome()
            else -> Unit
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(state)
        }
        UiState.Error -> {
            APErrorScreen(
                onClick = { viewModel.onAction(PreferenceSetupAction.OnRetryClicked) }
            )
        }
        UiState.Success -> {
            PreferenceSetupScreen(
                state = state,
                onAction = viewModel::onAction
            )
        }
    }

    bottomSheetData?.let { data ->
        APFilterBottomSheet(
            metaData = metaData,
            initData = data.params,
            includeYearQuarter = true,
            includeGenres = true,
            initSheetType = data.sheetType,
            onDismiss = data.onDismiss,
            onApply = { data.onConfirm(it) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreferenceSetupScreen(
    state: PreferenceSetupState,
    onAction: (PreferenceSetupAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()

    LaunchedEffect(listState, state.animes.size) {
        snapshotFlow { listState.layoutInfo }
            .map { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                lastVisibleItemIndex >= totalItemsCount - 3
            }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && !state.isMoreAnimeLoading && state.animes.isNotEmpty()) {
                    onAction(PreferenceSetupAction.OnLoadMore)
                }
            }
    }

    Scaffold(
        topBar = { APSkipActionTopAppBar(onSkipClicked = { onAction(PreferenceSetupAction.OnSkipClicked) }) },
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
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
                Header()

                LazyColumn(
                    state = listState,
                    userScrollEnabled = state.animes.isNotEmpty(),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
                ) {
                    item {
                        Column {
                            SearchSection(
                                state = state,
                                onAction = onAction
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(
                                        top = dimensionResource(R.dimen.padding_large),
                                        bottom = dimensionResource(R.dimen.padding_small)
                                    ),
                                thickness = dimensionResource(R.dimen.border_width_thick),
                                color = AniPickSurface
                            )
                        }
                    }
                    if (state.animes.isNotEmpty()) {
                        items(state.animes) { anime ->
                            val ratedAnime = state.ratedAnimes.find { it.animeId == anime.animeId }

                            AnimeRatingCard(
                                initRating = ratedAnime?.rating ?: 0f,
                                anime = anime,
                                onRatingAdd = { onAction(PreferenceSetupAction.OnRatingAddClicked(it)) },
                                onRatingRemove = { onAction(PreferenceSetupAction.OnRatingRemoveClicked(it)) }
                            )
                        }
                        item {
                            if (state.isMoreAnimeLoading) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = dimensionResource(R.dimen.padding_default)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = AniPickPrimary
                                    )
                                }
                            }
                        }
                    } else {
                        item {
                            if (state.isMoreAnimeLoading) {
                                AnimeListSectionSkeleton()
                            } else {
                                APEmptyContent(
                                    modifier = Modifier
                                        .fillParentMaxSize(),
                                    comment = stringResource(R.string.empty_content)
                                )
                            }
                        }
                    }
                }
            }

            Footer(
                state = state,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.padding_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
    ) {
        Text(
            text = stringResource(R.string.preference_setup_title),
            style = AniPick20Bold.copy(color = AniPickBlack),
        )
        Text(
            text = stringResource(R.string.preference_setup_subtitle),
            style = AniPick14Normal.copy(color = AniPickSecondary),
        )
    }
}

@Composable
private fun SearchSection(
    state: PreferenceSetupState,
    onAction: (PreferenceSetupAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.padding_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_default))
    ) {
        Text(
            text = stringResource(R.string.preference_setup_rated_count, state.ratedAnimes.size),
            style = AniPick14Normal.copy(
                color = if (state.ratedAnimes.isEmpty()) AniPickGray100 else AniPickPoint
            )
        )
        APBaseTextField(
            state = state.searchText,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            onKeyboardAction = {
                onAction(PreferenceSetupAction.OnSearchClicked)
            },
            placeholder = stringResource(R.string.preference_setup_search),
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_small))
                ) {
                    Icon(
                        imageVector = SearchOutlineIcon,
                        contentDescription = stringResource(R.string.search_icon),
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onAction(PreferenceSetupAction.OnSearchClicked) }
                            .padding(dimensionResource(R.dimen.padding_extra_small))
                            .size(dimensionResource(R.dimen.icon_size_small)),
                        tint = APColors.TextGray
                    )
                    Icon(
                        imageVector = CloseIcon,
                        contentDescription = stringResource(R.string.close_icon),
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onAction(PreferenceSetupAction.OnClearTextClicked) }
                            .padding(dimensionResource(R.dimen.padding_extra_small))
                            .size(dimensionResource(R.dimen.icon_size_small)),
                        tint = APColors.TextGray
                    )
                }
            },
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            APFilterTriggerChip(
                title = if (state.yearFilter == "전체년도") stringResource(R.string.preference_setup_year) else state.yearFilter,
                isSelected = state.yearFilter != "전체년도",
                onClick = { onAction(PreferenceSetupAction.OnFilterChipClicked(BottomSheetType.YEAR_QUARTER)) }
            )
            APFilterTriggerChip(
                title = if (state.quarterFilter.name == "전체분기") stringResource(R.string.preference_setup_quarter) else state.quarterFilter.name,
                isSelected = state.quarterFilter.name != "전체분기",
                onClick = { onAction(PreferenceSetupAction.OnFilterChipClicked(BottomSheetType.YEAR_QUARTER)) }
            )
            APFilterTriggerChip(
                title = if (state.genreFilter.name == "") stringResource(R.string.preference_setup_genre) else state.genreFilter.name,
                isSelected = state.genreFilter.name != "",
                onClick = { onAction(PreferenceSetupAction.OnFilterChipClicked(BottomSheetType.GENRE)) }
            )
        }
    }
}

@Composable
private fun Footer(
    state: PreferenceSetupState,
    onAction: (PreferenceSetupAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(bottom = dimensionResource(R.dimen.padding_extra_large)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_32))
    ) {
        HorizontalDivider(color = AniPickGray100)
        APPrimaryActionButton(
            text = stringResource(R.string.preference_setup_complete_btn),
            onClick = { onAction(PreferenceSetupAction.OnCompleteClicked) },
            isLoading = state.isRating,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_large))
        )
    }
}

@DevicePreviews
@Composable
private fun PreferenceSetupScreenPreview() {
    PreferenceSetupScreen(
        state = PreferenceSetupState(
            uiState = UiState.Success
        ),
        onAction = {}
    )
}