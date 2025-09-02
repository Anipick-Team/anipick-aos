package com.jparkbro.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.detail.DetailRepository
import com.jparkbro.model.detail.AnimeActorsResponse
import com.jparkbro.model.detail.DetailActor
import com.jparkbro.model.detail.DetailStudio
import com.jparkbro.model.detail.StudioAnime
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AnimeActorsViewModel.Factory::class)
class AnimeActorsViewModel @AssistedInject constructor(
    private val detailRepository: DetailRepository,
    @Assisted val animeId: Int,
) : ViewModel() {

    private val _response = MutableStateFlow<AnimeActorsResponse?>(null)
    val response = _response.asStateFlow()

    private val _actors = MutableStateFlow<List<DetailActor>>(emptyList())
    val actors = _actors.asStateFlow()

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
            detailRepository.getAnimeActors(
                animeId = animeId,
                cursor = _response.value?.cursor
            ).fold(
                onSuccess = {
                    _response.value = it
                    _actors.value += it.characters

                    _isLoading.value = false
                    _hasMoreData.value = it.characters.size == 18
                },
                onFailure = {
                    _isLoading.value = false
                    _hasMoreData.value = true
                }
            )
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(
            animeId: Int
        ): AnimeActorsViewModel
    }
}