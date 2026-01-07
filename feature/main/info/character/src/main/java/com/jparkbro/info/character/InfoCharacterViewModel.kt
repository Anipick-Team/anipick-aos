package com.jparkbro.info.character

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.anime.AnimeRepository
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
class InfoCharacterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _animeId = savedStateHandle.get<Long>("animeId")

    private val _state = MutableStateFlow(InfoCharacterState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<InfoCharacterEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        initDataLoad()
    }

    fun onAction(action: InfoCharacterAction) {
        when (action) {
            InfoCharacterAction.OnRetryClicked -> initDataLoad()
            InfoCharacterAction.LoadMoreCasts -> loadMore()
        }
    }

    private fun initDataLoad() {
        /*_state.update { InfoCharacterState() }

        viewModelScope.launch(Dispatchers.IO) {
            animeRepository.getAnimeSeries(
                animeId = _animeId ?: 0,
                cursor = _state.value.cursor
            ).fold(
                onSuccess = { result ->
                    _state.update {
                        it.copy(
                            uiState = UiState.Success,
                            cursor = result.cursor,
                            casts = result.animes,
                            hasMoreData = result.count > result.animes.size
                        )
                    }
                },
                onFailure = {
                    // TODO toast
                    _state.update { it.copy(uiState = UiState.Error) }
                }
            )
        }*/
    }

    private fun loadMore() {
        /*if (!_state.value.hasMoreData || _state.value.isLoading) return

        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            animeRepository.getAnimeSeries(
                animeId = _animeId ?: 0,
                cursor = _state.value.cursor
            ).fold(
                onSuccess = { result ->
                    _state.update {
                        it.copy(
                            cursor = result.cursor,
                            animes = it.animes + result.animes,
                            hasMoreData = result.count > (it.animes + result.animes).size,
                            isLoading = false
                        )
                    }
                },
                onFailure = {
                    // TODO toast
                    _state.update { it.copy(isLoading = false) }
                }
            )
        }*/
    }
}