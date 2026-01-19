package com.jparkbro.ranking

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.model.common.MetaData
import com.jparkbro.model.common.UiState
import com.jparkbro.ranking.components.SkeletonScreen
import com.jparkbro.ui.components.APErrorScreen

@Composable
internal fun RankingRoot(
    metaData: MetaData,
    bottomNav: @Composable () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    viewModel: RankingViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                bottomNav = bottomNav,
                onNavigateToSearch = onNavigateToSearch,
            )
        }
        UiState.Error -> {
            APErrorScreen(
                onClick = { viewModel.onAction(RankingAction.OnRetryClicked) }
            )
        }
        UiState.Success -> {
            RankingScreen(
                metaData = metaData,
                bottomNav = bottomNav,
                state = state,
                onAction = { action ->
                    when (action) {
                        RankingAction.NavigateToSearch -> onNavigateToSearch()
                        is RankingAction.NavigateToInfoAnime -> onNavigateToInfoAnime(action.animeId)
                    }
                    viewModel.onAction(action)
                }
            )
        }
    }
}

@Composable
private fun RankingScreen(
    metaData: MetaData,
    bottomNav: @Composable () -> Unit,
    state: RankingState,
    onAction: (RankingAction) -> Unit
) {

}