package com.jparkbro.mypage.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.actor.ActorRepository
import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.data.review.ReviewRepository
import com.jparkbro.data.user.UserRepository
import com.jparkbro.model.enum.ApiAction
import com.jparkbro.model.common.UiState
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentResult
import com.jparkbro.model.enum.DialogType
import com.jparkbro.model.enum.ReviewSortType
import com.jparkbro.model.enum.UserContentType
import com.jparkbro.ui.R
import com.jparkbro.ui.model.DialogData
import com.jparkbro.ui.model.SnackBarData
import com.jparkbro.ui.snackbar.GlobalSnackbarManager
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
class UserContentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val globalSnackbarManager: GlobalSnackbarManager,
    private val userRepository: UserRepository,
    private val animeRepository: AnimeRepository,
    private val reviewRepository: ReviewRepository,
    private val actorRepository: ActorRepository,
) : ViewModel() {

    private val contentType = savedStateHandle.get<UserContentType>("contentType")

    private val _state = MutableStateFlow(UserContentState(contentType = contentType))
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<UserContentEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        dataLoad()
        collectUserContent()
    }

    fun onAction(action: UserContentAction) {
        when (action) {
            UserContentAction.OnRetryClicked -> retry()
            UserContentAction.OnLoadMore -> dataLoad(true)
            is UserContentAction.OnChangeSortType -> changeSortType(action.type)
            is UserContentAction.OnReviewLikeClicked -> {
                updateReviewLikeState(
                    animeId = action.animeId,
                    reviewId = action.reviewId,
                    liked = action.isLiked,
                )
            }
            is UserContentAction.OnReviewDeleteClicked -> deleteReviewDialog(action.reviewId, action.animeId)
        }
    }

    private fun collectUserContent() {
        viewModelScope.launch(Dispatchers.Main) {
            userRepository.getUserContent().collect { userContent ->
                _state.update { state ->
                    val currentSize = when (state.contentType) {
                        UserContentType.RATING_REVIEW -> userContent.reviews.size
                        UserContentType.LIKED_PERSON -> userContent.actors.size
                        else -> userContent.animes.size
                    }
                    val totalCount = userContent.count ?: 0
                    state.copy(
                        count = userContent.count,
                        cursor = userContent.cursor,
                        animes = userContent.animes,
                        reviews = userContent.reviews,
                        actors = userContent.actors,
                        hasMoreData = currentSize < totalCount
                    )
                }
            }
        }
    }

    private fun dataLoad(isLoadMore: Boolean = false) {
        if (_state.value.isMoreDataLoading || !_state.value.hasMoreData) return

        if (isLoadMore) _state.update { it.copy(isMoreDataLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            if (contentType == null) {
                _state.update { it.copy(uiState = UiState.Error) }
                return@launch
            }

            val request = GetUserContentRequest(
                contentType = contentType,
                lastId = if (isLoadMore) _state.value.cursor?.lastId else null,
                lastLikeCount = if (_state.value.reviewSort == ReviewSortType.LIKES && isLoadMore) _state.value.cursor?.lastValue else null,
                lastRating = if ((_state.value.reviewSort == ReviewSortType.RATING_DESC || _state.value.reviewSort == ReviewSortType.RATING_ASC) && isLoadMore) _state.value.cursor?.lastValue else null,
                sort = _state.value.reviewSort,
            )

            val result = when (contentType) {
                UserContentType.WATCHLIST,
                UserContentType.WATCHING,
                UserContentType.FINISHED,
                UserContentType.LIKED_ANIME -> animeRepository.loadUserContentAnimes(request)
                UserContentType.LIKED_PERSON -> actorRepository.loadUserContentActors(request)
                UserContentType.RATING_REVIEW -> reviewRepository.loadUserContentReviews(request)
            }

            result.fold(
                onSuccess = {
                    _state.update { it.copy(
                        uiState = UiState.Success,
                        isMoreDataLoading = false
                    ) }
                },
                onFailure = {
                    if (isLoadMore) {
                        // TODO Toast
                        _state.update { it.copy(isMoreDataLoading = false) }
                    } else {
                        _state.update { it.copy(uiState = UiState.Error) }
                    }
                }
            )
        }
    }

    private fun updateReviewLikeState(animeId: Long, reviewId: Long, liked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.updateReviewLike(
                action = if (liked) ApiAction.CREATE else ApiAction.DELETE,
                reviewId = reviewId,
                animeId = animeId
            ).getOrThrow()
        }
    }

    private fun changeSortType(type: ReviewSortType) {
        if (_state.value.reviewSort == type) return

        _state.update {
            it.copy(
                reviewSort = type,
                hasMoreData = true,
            )
        }
        userRepository.userContentCache.update {
            GetUserContentResult(
                count = 0,
                cursor = null,
                reviews = emptyList(),
            )
        }
        dataLoad()
    }

    private fun deleteReviewDialog(reviewId: Long, animeId: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            _eventChannel.send(
                UserContentEvent.ShowDialog(
                    dialogData = DialogData(
                        type = DialogType.CONFIRM,
                        title = UiText.StringResource(R.string.dialog_delete_review_title),
                        subTitle = UiText.StringResource(R.string.dialog_delete_review_subtitle),
                        dismiss = UiText.StringResource(R.string.dialog_delete_review_dismiss),
                        confirm = UiText.StringResource(R.string.dialog_delete_review_confirm),
                        onConfirm = { deleteReview(reviewId, animeId) }
                    )
                )
            )
        }
    }

    private fun deleteReview(reviewId: Long, animeId: Long) {
        _state.update { it.copy(isApiLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.deleteReview(reviewId, animeId).fold(
                onSuccess = {
                    globalSnackbarManager.showSnackbar(
                        SnackBarData(
                            text = UiText.StringResource(R.string.snackbar_delete_review_success)
                        )
                    )
                },
                onFailure = { exception ->
                    globalSnackbarManager.showSnackbar(
                        SnackBarData(
                            text = UiText.StringResource(R.string.snackbar_http_500_error)
                        )
                    )
                }
            )
            _state.update { it.copy(isApiLoading = false) }
        }
    }

    private fun retry() {
        _state.update { it.copy(uiState = UiState.Loading) }

        dataLoad()
    }

}