package com.jparkbro.info.anime

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.actor.ActorRepository
import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.data.review.ReviewRepository
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.review.toReview
import com.jparkbro.model.dto.info.GetInfoReviewsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoAnimeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val animeRepository: AnimeRepository,
    private val actorRepository: ActorRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _animeId = savedStateHandle.get<Int>("animeId")

    private val _state = MutableStateFlow(InfoAnimeState())
    val state = _state.asStateFlow()

    private val _eventChannel = MutableSharedFlow<InfoAnimeEvent>()
    val events = _eventChannel.asSharedFlow()

    init {
        collectAnimeInfo()
        collectMyReview()
        collectAnimeReviews()
        initDataLoad()
    }

    fun onAction(action: InfoAnimeAction) {
        when (action) {
            InfoAnimeAction.LoadMoreReviews -> {}
            is InfoAnimeAction. OnAnimeLikeClicked -> {}
            is InfoAnimeAction.OnWatchStatusClicked -> {}
            is InfoAnimeAction.OnRatingChanged -> {}
            is InfoAnimeAction.OnChangeReviewSortType -> {}
            is InfoAnimeAction.OnReviewLikeClicked -> {}
            is InfoAnimeAction.OnReviewDeleteClicked -> {}
            is InfoAnimeAction.OnReviewEditClicked -> {}
            is InfoAnimeAction.OnReviewReportClicked -> {}
            is InfoAnimeAction.OnUserBlockClicked -> {}
        }
    }

    private fun collectAnimeInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            animeRepository.detailInfo.collect { result ->
                _state.update { it.copy(animeInfo = result) }
            }
        }
    }

    private fun collectMyReview() {
        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.detailMyReview.collect { result ->
                _state.update { it.copy(myReview = result) }
            }
        }
    }

    private fun collectAnimeReviews() {
        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.getAnimeReviews(_animeId ?: 0).collect { result ->
                result?.let { response ->
                    _state.update {
                        it.copy(
                            reviews = response.reviews.map { it.toReview() },
                            reviewCursor = response.cursor,
                            reviewCount = response.count
                        )
                    }
                }
            }
        }
    }

    private fun initDataLoad() {
        _state.update {
            it.copy(
                uiState = UiState.Loading
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                coroutineScope {
                    launch { getAnimeInfo() }
                    launch { getAnimeActor() }
                    launch { getAnimeSeries() }
                    launch { getRecommendedAnime() }
                    launch { getMyReview() }
                    launch { getAnimeReviews() }
                }

                _state.update { it.copy(uiState = UiState.Success) }

            } catch (e: Exception) {
                _state.update { it.copy(uiState = UiState.Error) }
            }
        }
    }

    private suspend fun getAnimeInfo() {
        animeRepository.getDetailInfo(_animeId ?: 0)
            .onFailure { throw it }
    }

    private suspend fun getAnimeActor() {
        actorRepository.getDetailActor(_animeId ?: 0)
            .fold(
                onSuccess = { actors ->
                    _state.update { it.copy(casts = actors) }
                },
                onFailure = { throw it }
            )
    }

    private suspend fun getAnimeSeries() {
        animeRepository.getDetailSeries(_animeId ?: 0)
            .fold(
                onSuccess = { animes ->
                    _state.update { it.copy(series = animes) }
                },
                onFailure = { throw it }
            )
    }

    private suspend fun getRecommendedAnime() {
        animeRepository.getDetailRecommendation(_animeId ?: 0)
            .fold(
                onSuccess = { animes ->
                    _state.update { it.copy(recommendations = animes) }
                },
                onFailure = { throw it }
            )
    }

    private suspend fun getMyReview() {
        reviewRepository.getAnimeDetailMyReview(_animeId ?: 0)
            .onFailure { throw it }

    }

    private suspend fun getAnimeReviews() {
        reviewRepository.loadAnimeReviews(
            animeId = _animeId ?: 0,
            request = GetInfoReviewsRequest(
                animeId = _animeId ?: 0,
                sort = _state.value.reviewSort.param
            )
        ).onFailure { throw it }
    }

    private fun loadMoreReviews() {
        val currentCursor = _state.value.reviewCursor
        val currentState = _state.value

        if (currentState.reviewCount == currentState.reviews.size || currentState.isLoadingMoreReviews) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoadingMoreReviews = true) }

            reviewRepository.loadAnimeReviews(
                animeId = _animeId ?: 0,
                request = GetInfoReviewsRequest(
                    animeId = _animeId ?: 0,
                    sort = currentState.reviewSort.param,
                    lastValue = currentCursor?.lastValue,
                    lastId = currentCursor?.lastId,
                )
            ).fold(
                onSuccess = {
                    _state.update { it.copy(isLoadingMoreReviews = false) }
                },
                onFailure = {
                    _state.update { it.copy(isLoadingMoreReviews = false) }
                    // TODO 에러 처리
                }
            )
        }
    }
}