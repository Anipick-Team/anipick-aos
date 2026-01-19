package com.jparkbro.studio

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.studio.StudioRepository
import com.jparkbro.model.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudioViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val studioRepository: StudioRepository,
) : ViewModel() {

    private val _studioId = savedStateHandle.get<Long>("studioId")

    private val _state = MutableStateFlow(StudioState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<StudioEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        initDataLoad()
    }

    fun onAction(action: StudioAction) {
        when (action) {
            StudioAction.OnRetryClicked -> initDataLoad()
            StudioAction.LoadMoreAnime -> loadMore()
        }
    }

    private fun initDataLoad() {
        _state.update { StudioState() }

        viewModelScope.launch(Dispatchers.IO) {
            studioRepository.getStudioInfo(
                studioId = _studioId ?: 0L,
                cursor = _state.value.cursor
            ).fold(
                onSuccess = { result ->
                    _state.update {
                        it.copy(
                            uiState = UiState.Success,
                            studioName = result.studioName,
                            cursor = result.cursor,
                            animes = result.animes,
                            hasMoreData = result.animes.size >= 18
                        )
                    }
                },
                onFailure = {
                    // TODO toast
                    _state.update { it.copy(uiState = UiState.Error) }
                }
            )
        }
    }

    private fun loadMore() {
        if (!_state.value.hasMoreData || _state.value.isLoading) return

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            studioRepository.getStudioInfo(
                studioId = _studioId ?: 0L,
                cursor = _state.value.cursor
            ).fold(
                onSuccess = { result ->
                    _state.update {
                        it.copy(
                            cursor = result.cursor,
                            animes = it.animes + result.animes,
                            hasMoreData = result.animes.size >= 18,
                            isLoading = false
                        )
                    }
                },
                onFailure = {
                    // TODO toast
                    _state.update { it.copy(isLoading = false) }
                }
            )
        }
    }
}