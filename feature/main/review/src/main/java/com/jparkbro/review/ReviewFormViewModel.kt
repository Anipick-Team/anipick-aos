package com.jparkbro.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.review.ReviewRepository
import com.jparkbro.model.review.EditMyReviewRequest
import com.jparkbro.model.review.MyReview
import com.jparkbro.review.navigation.ReviewForm
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ReviewFormViewModel.Factory::class)
class ReviewFormViewModel @AssistedInject constructor(
    private val reviewRepository: ReviewRepository,
    @Assisted val infoData: ReviewForm
) : ViewModel() {

    private val _review = MutableStateFlow<MyReview?>(null)
    val review: StateFlow<MyReview?> = _review.asStateFlow()

    private val _newRating = MutableStateFlow(0f)
    val newRating: StateFlow<Float> = _newRating.asStateFlow()

    private val _newReviewContent = MutableStateFlow("")
    val newReviewContent: StateFlow<String> = _newReviewContent.asStateFlow()

    private val _includeSpoiler = MutableStateFlow(false)
    val includeSpoiler: StateFlow<Boolean> = _includeSpoiler.asStateFlow()

    fun updateRating(rating: Float) {
        _newRating.value = rating
    }

    fun updateReviewContent(content: String) {
        _newReviewContent.value = content
    }

    fun toggleIncludeSpoiler() {
        _includeSpoiler.value = !_includeSpoiler.value
    }

    init {
        getMyReview()
    }

    private fun getMyReview() {
        viewModelScope.launch {
            reviewRepository.getMyReview(infoData.animeId).fold(
                onSuccess = {
                    _review.value = it
                    _newRating.value = it.rating
                    _newReviewContent.value = it.content ?: ""
                    _includeSpoiler.value = it.isSpoiler
                },
                onFailure = {
                    // TODO
                }
            )
        }
    }

    fun editReview(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            reviewRepository.editMyReview(
                animeId = infoData.animeId,
                request = EditMyReviewRequest(
                    content = _newReviewContent.value,
                    rating = _newRating.value,
                    isSpoiler = _includeSpoiler.value
                )
            ).fold(
                onSuccess = {
                    onResult(true)
                },
                onFailure = {
                    // TODO
                    onResult(false)
                }
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            infoData: ReviewForm
        ): ReviewFormViewModel
    }
}