package com.jparkbro.actor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.actor.ActorRepository
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
class ActorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val actorRepository: ActorRepository,
) : ViewModel() {

    private val _actorId = savedStateHandle.get<Long>("actorId")

    private val _state = MutableStateFlow(ActorState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<ActorEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        initDataLoad()
    }

    fun onAction(action: ActorAction) {
        when (action) {
            ActorAction.OnRetryClicked -> initDataLoad()
            ActorAction.LoadMoreAnime -> loadMore()
            is ActorAction.OnActorLikeClicked -> updateActorLike(action.isLiked)
        }
    }

    private fun initDataLoad() {
        _state.update { ActorState() }

        viewModelScope.launch(Dispatchers.IO) {
            actorRepository.getActor(
                personId = _actorId ?: 0L,
                cursor = _state.value.result.cursor
            ).fold(
                onSuccess = { result ->
                    _state.update {
                        it.copy(
                            uiState = UiState.Success,
                            result = result,
                            works = result.works
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
            actorRepository.getActor(
                personId = _actorId ?: 0L,
                cursor = _state.value.result.cursor
            ).fold(
                onSuccess = { result ->
                    _state.update {
                        it.copy(
                            result = result,
                            works = it.works + result.works,
                            hasMoreData = result.works.size >= 18,
                            isLoading = false,
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

    private fun updateActorLike(isLiked: Boolean) {
        _state.update { it.copy(isLikedLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            actorRepository.updateActorLike(
                personId = _actorId ?: 0,
                isLiked = isLiked,
            ).fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            result = it.result.copy(
                                isLiked = !isLiked
                            )
                        )
                    }
                },
                onFailure = {
                    // TODO toast
                }
            )
            _state.update { it.copy(isLikedLoading = false) }
        }
    }
}