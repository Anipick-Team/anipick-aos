package com.jparkbro.mypage.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.actor.ActorRepository
import com.jparkbro.data.anime.AnimeRepository
import com.jparkbro.data.review.ReviewRepository
import com.jparkbro.data.user.UserRepository
import com.jparkbro.model.common.ApiAction
import com.jparkbro.model.common.UiState
import com.jparkbro.model.dto.mypage.usercontent.GetUserContentRequest
import com.jparkbro.model.enum.UserContentType
import com.jparkbro.ui.snackbar.GlobalSnackbarManager
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
        initDataLoad()
        collectUserContent()
    }

    fun onAction(action: UserContentAction) {
        when (action) {
            is UserContentAction.OnReviewLikeClicked -> updateReviewLikeState(
                animeId = action.animeId,
                reviewId = action.reviewId,
                liked = action.isLiked,
                onResult = { action.callback(it) }
            )
        }
    }

    private fun collectUserContent() {
        viewModelScope.launch(Dispatchers.Main) {
            userRepository.getUserContent().collect { userContent ->
                _state.update {
                    it.copy(
                        count = userContent.count,
                        cursor = userContent.cursor,
                        animes = userContent.animes,
                        reviews = userContent.reviews,
                        actors = userContent.actors
                    )
                }
            }
        }
    }

    private fun initDataLoad() {
        _state.update { it.copy(uiState = UiState.Loading) }

        viewModelScope.launch(Dispatchers.IO) {
            if (contentType == null) {
                _state.update { it.copy(uiState = UiState.Error) }
                return@launch
            }

            val request = GetUserContentRequest(
                contentType = contentType,
                lastId = _state.value.cursor?.lastId,
                lastLikeCount = _state.value.cursor?.lastValue,
                lastRating = _state.value.cursor?.lastValue,
                sort = _state.value.reviewSort.param,
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
                onSuccess = { _state.update { it.copy(uiState = UiState.Success) } },
                onFailure = { _state.update { it.copy(uiState = UiState.Error) } }
            )
        }
    }

    private fun updateReviewLikeState(animeId: Long, reviewId: Long, liked: Boolean, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.updateReviewLike(
                action = if (liked) ApiAction.CREATE else ApiAction.DELETE,
                reviewId = reviewId,
                animeId = animeId
            ).getOrThrow()

            onResult(true)
        }
    }
}