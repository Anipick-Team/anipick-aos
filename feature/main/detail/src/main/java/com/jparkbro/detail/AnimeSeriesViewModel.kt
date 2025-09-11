package com.jparkbro.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.detail.DetailRepository
import com.jparkbro.detail.navigation.AnimeSeries
import com.jparkbro.model.detail.AnimeSeriesResponse
import com.jparkbro.model.detail.DetailSeries
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AnimeSeriesViewModel.Factory::class)
class AnimeSeriesViewModel @AssistedInject constructor(
    private val detailRepository: DetailRepository,
    @Assisted val initData: AnimeSeries,
) : ViewModel() {

    private val _response = MutableStateFlow<AnimeSeriesResponse?>(null)
    val response = _response.asStateFlow()

    private val _animeList = MutableStateFlow<List<DetailSeries>>(emptyList())
    val animeList = _animeList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _hasMoreData = MutableStateFlow(true)

    init {
        loadData()
    }

    fun loadData() {
        if (_isLoading.value || !_hasMoreData.value) return

        _isLoading.value = true

        viewModelScope.launch {
            detailRepository.getAnimeSeries(
                animeId = initData.animeId,
                cursor = _response.value?.cursor
            ).fold(
                onSuccess = {
                    _response.value = it
                    _animeList.value += it.animes

                    _isLoading.value = false
                    _hasMoreData.value = it.animes.size == 18
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
            initData: AnimeSeries
        ): AnimeSeriesViewModel
    }
}