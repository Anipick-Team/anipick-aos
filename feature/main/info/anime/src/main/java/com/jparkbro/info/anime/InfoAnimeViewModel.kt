package com.jparkbro.info.anime

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.actor.ActorRepository
import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.data.review.ReviewRepository
import com.jparkbro.model.common.ApiAction
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.review.toReview
import com.jparkbro.model.dto.info.GetInfoReviewsRequest
import com.jparkbro.model.dto.info.ReviewRatingRequest
import com.jparkbro.model.enum.DialogType
import com.jparkbro.model.enum.WatchStatus
import com.jparkbro.model.review.ReportReviewRequest
import com.jparkbro.ui.R
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val _animeId = savedStateHandle.get<Long>("animeId")

    private val _state = MutableStateFlow(InfoAnimeState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<InfoAnimeEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        collectAnimeInfo()
        collectMyReview()
        collectAnimeReviews()
        initDataLoad()
        saveRecentAnime()
    }

    fun onAction(action: InfoAnimeAction) {
        when (action) {
            InfoAnimeAction.OnRetryClicked -> viewModelScope.launch(Dispatchers.IO) { retry() }
            InfoAnimeAction.LoadMoreReviews -> loadMoreReviews()
            is InfoAnimeAction.OnAnimeLikeClicked -> updateAnimeLike(action.isLiked)
            is InfoAnimeAction.OnWatchStatusClicked -> updateWatchStatus(action.watchStatus)
            is InfoAnimeAction.OnRatingChanged -> updateRating(action.rating, action.onFailure)
            is InfoAnimeAction.OnChangeReviewSortType -> {
                if (_state.value.reviewSort != action.sortType) {
                    _state.update { it.copy(reviewSort = action.sortType) }
                    viewModelScope.launch(Dispatchers.IO) { getAnimeReviews() }
                }
            }
            is InfoAnimeAction.OnReviewLikeClicked -> updateLikeState(
                reviewId = action.reviewId,
                liked = action.isLiked,
                onResult = { action.callback(it) }
            )
            is InfoAnimeAction.OnReviewDeleteClicked -> deleteReviewDialog(action.reviewId)
            is InfoAnimeAction.OnReviewReportClicked -> reportReviewDialog(action.reviewId)
            is InfoAnimeAction.OnUserBlockClicked -> userBlockDialog(action.userId)
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
                            reviewCount = response.count ?: 0
                        )
                    }
                }
            }
        }
    }

    private fun saveRecentAnime() {
        viewModelScope.launch(Dispatchers.IO) {
            _animeId?.let { animeRepository.saveRecentAnime(it) }
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

    private fun updateAnimeLike(isLiked: Boolean) {
        _state.update { it.copy(isLikingAnime = true) }

        viewModelScope.launch(Dispatchers.IO) {
            animeRepository.updateAnimeLike(_animeId ?: 0, isLiked)
                .onFailure {  } // TODO 에러처리

            _state.update { it.copy(isLikingAnime = false) }
        }
    }

    private fun updateWatchStatus(watchStatus: WatchStatus) {
        _state.update { it.copy(isWatchStatusChanging = true) }

        viewModelScope.launch(Dispatchers.IO) {
            animeRepository.updateWatchStatus(
                animeId = _animeId ?: 0,
                watchStatus = watchStatus
            )

            _state.update { it.copy(isWatchStatusChanging = false) }
        }
    }

    private fun updateRating(rating: Float, onFailure: () -> Unit) {
        _state.update { it.copy(isAnimeRatingLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.updateAnimeRating(
                animeId = _animeId ?: 0,
                request = ReviewRatingRequest(rating)
            ).fold(
                onSuccess = {
                    // TODO Toast
                },
                onFailure = {
                    // TODO 에러처리
                    onFailure()
                }
            )

            _state.update { it.copy(isAnimeRatingLoading = false) }
        }
    }

    private fun updateLikeState(reviewId: Long, liked: Boolean, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.updateReviewLike(
                action = if (liked) ApiAction.CREATE else ApiAction.DELETE,
                reviewId = reviewId
            ).getOrThrow()

            onResult(true)
        }
    }

    private fun deleteReview(reviewId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.deleteReview(reviewId).fold(
                onSuccess = {
                    _eventChannel.send(
                        InfoAnimeEvent.ShowSnackBar(
                            SnackBarData(
                                text = UiText.StringResource(R.string.snackbar_delete_review_success)
                            )
                        )
                    )
                    getAnimeReviews()
                },
                onFailure = { exception ->
                    _eventChannel.send(
                        InfoAnimeEvent.ShowSnackBar(
                            SnackBarData(
                                text = UiText.StringResource(R.string.snackbar_delete_review_failed)
                            )
                        )
                    )
                }
            )
        }
    }

    private fun reportReview(reviewId: Long, reason: String) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.reportReview(reviewId, ReportReviewRequest(message = reason)).fold(
                onSuccess = {
                    _eventChannel.send(
                        InfoAnimeEvent.ShowSnackBar(
                            SnackBarData(
                                text = UiText.StringResource(R.string.snackbar_review_report_success)
                            )
                        )
                    )
                    getAnimeReviews()
                },
                onFailure = { exception ->
                    _eventChannel.send(
                        InfoAnimeEvent.ShowSnackBar(
                            SnackBarData(
                                text = UiText.StringResource(R.string.snackbar_review_report_failed)
                            )
                        )
                    )
                }
            )
        }
    }

    private fun blockUser(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.blockUser(userId).fold(
                onSuccess = {
                    _eventChannel.send(
                        InfoAnimeEvent.ShowSnackBar(
                            SnackBarData(
                                text = UiText.StringResource(R.string.snackbar_user_block_success)
                            )
                        )
                    )
                    getAnimeReviews()
                },
                onFailure = { exception ->
                    _eventChannel.send(
                        InfoAnimeEvent.ShowSnackBar(
                            SnackBarData(
                                text = UiText.StringResource(R.string.snackbar_user_block_failed)
                            )
                        )
                    )
                }
            )
        }
    }

    private fun reportReviewDialog(reviewId: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                InfoAnimeEvent.ShowDialog(
                    dialogData = DialogData(
                        type = DialogType.CONFIRM,
                        title = UiText.StringResource(R.string.dialog_report_review_title),
                        subTitle = UiText.StringResource(R.string.dialog_report_review_subtitle),
                        dismiss = UiText.StringResource(R.string.dialog_report_review_dismiss),
                        confirm = UiText.StringResource(R.string.dialog_report_review_next),
                        onConfirm = {
                            reportReviewReasonDialog(reviewId)
                        }
                    )
                )
            )
        }
    }

    private fun reportReviewReasonDialog(reviewId: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                InfoAnimeEvent.ShowDialog(
                    dialogData = DialogData(
                        type = DialogType.SELECT,
                        title = UiText.StringResource(R.string.dialog_report_review_reason_title),
                        subTitle = UiText.StringResource(R.string.dialog_report_review_subtitle),
                        dismiss = UiText.StringResource(R.string.dialog_report_review_dismiss),
                        confirm = UiText.StringResource(R.string.dialog_report_review_confirm),
                        onConfirm = { reason ->
                            reportReview(reviewId, reason.toString())
                        }
                    )
                )
            )
        }
    }

    private fun deleteReviewDialog(reviewId: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                InfoAnimeEvent.ShowDialog(
                    dialogData = DialogData(
                        type = DialogType.CONFIRM,
                        title = UiText.StringResource(R.string.dialog_delete_review_title),
                        subTitle = UiText.StringResource(R.string.dialog_delete_review_subtitle),
                        dismiss = UiText.StringResource(R.string.dialog_delete_review_dismiss),
                        confirm = UiText.StringResource(R.string.dialog_delete_review_confirm),
                        onConfirm = { deleteReview(reviewId) }
                    )
                )
            )
        }
    }

    private fun userBlockDialog(userId: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                InfoAnimeEvent.ShowDialog(
                    dialogData = DialogData(
                        type = DialogType.CONFIRM,
                        title = UiText.StringResource(R.string.dialog_user_block_title),
                        subTitle = UiText.StringResource(R.string.dialog_user_block_subtitle),
                        dismiss = UiText.StringResource(R.string.dialog_user_block_dismiss),
                        confirm = UiText.StringResource(R.string.dialog_user_block_confirm),
                        onConfirm = { blockUser(userId) }
                    )
                )
            )
        }
    }

    private fun retry() {
        _state.update {
            it.copy(
                uiState = UiState.Loading
            )
        }
        initDataLoad()
    }
}