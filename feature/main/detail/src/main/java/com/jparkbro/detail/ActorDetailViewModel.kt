package com.jparkbro.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.detail.DetailRepository
import com.jparkbro.model.common.ApiAction
import com.jparkbro.model.detail.ActorDetailResponse
import com.jparkbro.model.detail.ActorFilmography
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ActorDetailViewModel.Factory::class)
class ActorDetailViewModel @AssistedInject constructor(
    private val detailRepository: DetailRepository,
    @Assisted val actorId: Int,
) : ViewModel() {

    private val _response = MutableStateFlow<ActorDetailResponse?>(null)
    val response = _response.asStateFlow()

    private val _workList = MutableStateFlow<List<ActorFilmography>>(emptyList())
    val workList = _workList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _hasMoreData = MutableStateFlow(true)

    init {
        loadData(true)
    }

    fun loadData(init: Boolean = false) {
        if (_isLoading.value || !_hasMoreData.value) return

        if (!init) _isLoading.value = true

        viewModelScope.launch {
            detailRepository.getActorInfo(
                personId = actorId,
                cursor = _response.value?.cursor
            ).fold(
                onSuccess = {
                    _response.value = it
                    _workList.value += it.works

                    _isLoading.value = false
                    _hasMoreData.value = it.works.size == 18
                },
                onFailure = {
                    _isLoading.value = false
                    _hasMoreData.value = true
                }
            )
        }
    }

    private val _isLikeLoading = MutableStateFlow(false)
    val isLikeLoading = _isLikeLoading.asStateFlow()
    fun likePerson(isLiked: Boolean) {
        if (_isLikeLoading.value) return

        viewModelScope.launch {
            _isLikeLoading.value = true
            _response.value = _response.value?.isLiked?.let { _response.value?.copy(isLiked = !it) }
            detailRepository.setLikeActor(
                action = if (isLiked) {
                    ApiAction.DELETE
                } else {
                    ApiAction.CREATE
                },
                personId = actorId
            ).fold(
                onSuccess = {
                    _isLikeLoading.value = false
                },
                onFailure = {
                    _response.value = _response.value?.isLiked?.let { _response.value?.copy(isLiked = !it) }
                    _isLikeLoading.value = false
                }
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            actorId: Int
        ): ActorDetailViewModel
    }
}