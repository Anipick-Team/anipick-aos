package com.jparkbro.info.character

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jparkbro.info.character.components.SkeletonScreen
import com.jparkbro.model.common.UiState
import com.jparkbro.ui.components.APErrorScreen

@Composable
internal fun InfoCharacterRoot(
    onNavigateBack: () -> Unit,
    viewModel: InfoCharacterViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.uiState) {
        UiState.Loading -> {
            SkeletonScreen(
                onAction = { action ->
                    when (action) {
                        InfoCharacterAction.NavigateBack -> onNavigateBack()
                    }
                }
            )
        }

        UiState.Error -> {
            APErrorScreen(
                onClick = { viewModel.onAction(InfoCharacterAction.OnRetryClicked) }
            )
        }

        UiState.Success -> {
            InfoCharacterScreen(
                state = state,
                onAction = { action ->
                    when (action) {
                        InfoCharacterAction.NavigateBack -> onNavigateBack()
                        is InfoCharacterAction.NavigateToActor -> {}
                    }
                    viewModel.onAction(action)
                }
            )
        }
    }
}

@Composable
private fun InfoCharacterScreen(
    state: InfoCharacterState,
    onAction: (InfoCharacterAction) -> Unit,
) {

}