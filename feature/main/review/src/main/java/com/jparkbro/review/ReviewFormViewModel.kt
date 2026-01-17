package com.jparkbro.review

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.review.ReviewRepository
import com.jparkbro.model.common.FormType
import com.jparkbro.model.common.UiState
import com.jparkbro.model.dto.review.SaveMyReviewRequest
import com.jparkbro.ui.R
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
class ReviewFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reviewRepository: ReviewRepository,
    private val globalSnackbarManager: GlobalSnackbarManager
) : ViewModel() {

    private val _animeId = savedStateHandle.get<Long>("animeId")
    private val _formType = savedStateHandle.get<FormType>("formType")

    private val _state = MutableStateFlow(ReviewFormState(formType = _formType ?: FormType.CREATE))
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<ReviewFormEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        loadMyReview()
    }

    fun onAction(action: ReviewFormAction) {
        when (action) {
            is ReviewFormAction.OnRatingChanged -> {
                _state.update { it.copy(animeReview = it.animeReview?.copy(rating = action.rating)) }
            }

            ReviewFormAction.OnSpoilerClicked -> {
                _state.update { it.copy(animeReview = it.animeReview?.copy(isSpoiler = !(it.animeReview.isSpoiler))) }
            }

            ReviewFormAction.OnSaveReviewClicked -> saveReview()
        }
    }

    private fun loadMyReview() {
        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.getReviewFormAnimeReview(
                animeId = _animeId ?: 0
            ).fold(
                onSuccess = { review ->
                    _state.update {
                        it.copy(
                            uiState = UiState.Success,
                            animeReview = review,
                            content = TextFieldState(initialText = review.content ?: "")
                        )
                    }
                },
                onFailure = {
                    globalSnackbarManager.showSnackbar(
                        SnackBarData(
                            text = UiText.StringResource(R.string.snackbar_create_review_success)
                        )
                    )
                    _state.update { it.copy(uiState = UiState.Error) }
                }
            )
        }
    }

    private fun saveReview() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            reviewRepository.saveMyReview(
                animeId = _animeId ?: 0,
                request = SaveMyReviewRequest(
                    content = _state.value.content.text.toString(),
                    rating = _state.value.animeReview?.rating ?: 0f,
                    isSpoiler = _state.value.animeReview?.isSpoiler ?: false
                )
            ).fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false) }
                    globalSnackbarManager.showSnackbar(
                        SnackBarData(
                            text = UiText.StringResource(R.string.snackbar_create_review_success)
                        )
                    )
                    _eventChannel.send(ReviewFormEvent.NavigateBack)
                },
                onFailure = {
                    globalSnackbarManager.showSnackbar(
                        SnackBarData(
                            text = UiText.StringResource(R.string.snackbar_http_500_error)
                        )
                    )
                }
            )
        }
    }
}