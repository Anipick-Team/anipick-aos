package com.jparkbro.home.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.data.home.HomeRepository
import com.jparkbro.data.review.ReviewRepository
import com.jparkbro.model.common.ApiAction
import com.jparkbro.model.common.UiState
import com.jparkbro.model.enum.DialogType
import com.jparkbro.model.enum.HomeDetailType
import com.jparkbro.model.exception.ApiException
import com.jparkbro.model.home.HomeDetailRequest
import com.jparkbro.model.review.ReportReviewRequest
import com.jparkbro.ui.R
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.util.UiText
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
class HomeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val animeRepository: AnimeRepository,
    private val reviewRepository: ReviewRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val type = savedStateHandle.get<HomeDetailType>("type")

    private val _state = MutableStateFlow(HomeDetailState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<HomeDetailEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                type = type ?: HomeDetailType.RECOMMENDS
            )
        }
        loadDataByType()
        collectReviews()
    }

    private fun loadDataByType() {
        viewModelScope.launch(Dispatchers.IO) {
            when (type) {
                HomeDetailType.SIMILAR_TO_WATCHED -> {
                    getRecentAnime()
                    initLoad()
                }
                HomeDetailType.RECOMMENDS -> {
                    getNickname()
                    initLoad()
                }
                else -> {
                    initLoad()
                }
            }
        }
    }

    private fun collectReviews() {
        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.detailRecentReviews.collect { result ->
                _state.update {
                    it.copy(
                        reviews = result?.reviews ?: emptyList(),
                        cursor = result?.cursor,
                        hasMoreData = result?.reviews?.size != result?.count,
                        uiState = UiState.Success,
                        isMoreDataLoading = false
                    )
                }
            }
        }
    }

    fun onAction(action: HomeDetailAction) {
        when (action) {
            HomeDetailAction.OnRetryClicked -> viewModelScope.launch(Dispatchers.IO) { retry() }
            is HomeDetailAction.OnChangeSortType -> {
                _state.update { it.copy(sort = action.type) }
                viewModelScope.launch(Dispatchers.IO) { initLoad() }
            }
            HomeDetailAction.OnLoadMore -> loadMore()
            is HomeDetailAction.OnReviewLikeClicked -> updateLikeState(
                reviewId = action.reviewId,
                animeId = action.animeId,
                liked = action.isLiked,
                onResult = { action.callback(it) }
            )
            is HomeDetailAction.OnUserBlockClicked -> userBlockDialog(action.userId)
            is HomeDetailAction.OnReviewReportClicked -> reportReviewDialog(action.reviewId)
            is HomeDetailAction.OnReviewDeleteClicked -> deleteReviewDialog(action.reviewId)

        }
    }

    private suspend fun getNickname() {
        userPreferenceRepository.getUserNickName()
            .fold(
                onSuccess = { nickname ->
                    _state.update { it.copy(nickname = nickname) }
                },
                onFailure = {
                    throw it
                }
            )
    }

    private suspend fun getRecentAnime() {
        animeRepository.loadRecentAnime()
            .onSuccess { animeId ->
                _state.update { it.copy(recentAnime = animeId) }
            }
            .onFailure {
                throw it
            }
    }

    private suspend fun initLoad() {
        _state.update {
            it.copy(
                uiState = UiState.Loading,
                animes = emptyList(),
                isMoreDataLoading = false,
                hasMoreData = true,
                cursor = null
            )
        }

        when (type) {
            HomeDetailType.RECENT_REVIEWS -> {
                reviewRepository.loadDetailRecentReviews()
                    .onFailure { exception ->
                        _state.update { it.copy(uiState = UiState.Error) }
                    }
            }
            else -> {
                homeRepository.getDetailData(
                    type = type ?: HomeDetailType.RECOMMENDS,
                    request = HomeDetailRequest(
                        animeId = _state.value.recentAnime,
                        lastId = _state.value.cursor?.lastId,
                        lastValue = _state.value.cursor?.lastValue,
                        sort = _state.value.sort.param,
                    ),
                ).fold(
                    onSuccess = { response ->
                        _state.update {
                            it.copy(
                                animes = response.animes,
                                hasMoreData = response.animes.size >= 18,
                                referenceAnimeTitle = response.referenceAnimeTitle,
                                uiState = UiState.Success,
                                cursor = response.cursor
                            )
                        }
                    },
                    onFailure = {
                        _state.update {
                            it.copy(
                                uiState = UiState.Error // TODO 에러 종류 분기 처리
                            )
                        }
                    }
                )
            }
        }
    }

    private fun loadMore() {
        if (_state.value.isMoreDataLoading || !_state.value.hasMoreData) return

        _state.update { it.copy(isMoreDataLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            when (type) {
                HomeDetailType.RECENT_REVIEWS -> {
                    reviewRepository.loadDetailRecentReviews(_state.value.cursor)
                        .onFailure { exception ->
                            _state.update { it.copy(isMoreDataLoading = false) }
                        }
                }
                else -> {
                    homeRepository.getDetailData(
                        type = type ?: HomeDetailType.RECOMMENDS,
                        request = HomeDetailRequest(
                            animeId = _state.value.recentAnime,
                            lastId = _state.value.cursor?.lastId,
                            lastValue = _state.value.cursor?.lastValue,
                            sort = _state.value.sort.param,
                        ),
                    ).fold(
                        onSuccess = { response ->
                            _state.update {
                                it.copy(
                                    animes = it.animes + response.animes,
                                    hasMoreData = response.animes.size >= 18,
                                    referenceAnimeTitle = response.referenceAnimeTitle,
                                    uiState = UiState.Success,
                                    cursor = response.cursor,
                                    isMoreDataLoading = false,
                                )
                            }
                        },
                        onFailure = { exception ->
                            when (exception) {
                                is ApiException -> {
                                    _eventChannel.send(
                                        HomeDetailEvent.DataLoadFailed(exception.errorValue)
                                    )
                                }
                                else -> {
                                    _eventChannel.send(
                                        HomeDetailEvent.DataLoadFailed("Unknown error") // TODO
                                    )
                                }
                            }
                            _state.update {
                                it.copy(
                                    isMoreDataLoading = false
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    private fun updateLikeState(reviewId: Long, animeId: Long, liked: Boolean, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.updateReviewLike(
                action = if (liked) ApiAction.CREATE else ApiAction.DELETE,
                reviewId = reviewId,
                animeId = animeId
            ).getOrThrow()

            onResult(true)
        }
    }

    private fun deleteReview(reviewId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.deleteReview(reviewId).fold(
                onSuccess = {
                    _eventChannel.send(
                        HomeDetailEvent.ShowSnackBar(
                            SnackBarData(
                                text = UiText.StringResource(R.string.snackbar_delete_review_success)
                            )
                        )
                    )
                    initLoad()
                },
                onFailure = { exception ->
                    _eventChannel.send(
                        HomeDetailEvent.ShowSnackBar(
                            SnackBarData(
                                text = UiText.StringResource(R.string.snackbar_http_500_error)
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
                        HomeDetailEvent.ShowSnackBar(
                            SnackBarData(
                                text = UiText.StringResource(R.string.snackbar_review_report_success)
                            )
                        )
                    )
                    initLoad()
                },
                onFailure = { exception ->
                    _eventChannel.send(
                        HomeDetailEvent.ShowSnackBar(
                            SnackBarData(
                                text = UiText.StringResource(R.string.snackbar_http_500_error)
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
                        HomeDetailEvent.ShowSnackBar(
                            SnackBarData(
                                text = UiText.StringResource(R.string.snackbar_user_block_success)
                            )
                        )
                    )
                    initLoad()
                },
                onFailure = { exception ->
                    _eventChannel.send(
                        HomeDetailEvent.ShowSnackBar(
                            SnackBarData(
                                text = UiText.StringResource(R.string.snackbar_http_500_error)
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
                HomeDetailEvent.ShowDialog(
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
                HomeDetailEvent.ShowDialog(
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
                HomeDetailEvent.ShowDialog(
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
                HomeDetailEvent.ShowDialog(
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

    private suspend fun retry() {
        _state.update {
            it.copy(
                uiState = UiState.Loading
            )
        }
        initLoad()
    }
}