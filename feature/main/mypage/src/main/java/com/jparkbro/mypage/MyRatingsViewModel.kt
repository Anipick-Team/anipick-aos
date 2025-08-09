package com.jparkbro.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.mypage.MyPageRepository
import com.jparkbro.data.review.ReviewRepository
import com.jparkbro.model.common.ApiAction
import com.jparkbro.model.detail.ReviewSort
import com.jparkbro.model.mypage.MyReviewItem
import com.jparkbro.model.mypage.MyReviewsRequest
import com.jparkbro.model.mypage.MyReviewsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRatingsViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val reviewRepository: ReviewRepository,
) : ViewModel() {

    private val _sort = MutableStateFlow(ReviewSort.LATEST)
    val sort: StateFlow<ReviewSort> = _sort.asStateFlow()

    private val _isReviewOnly = MutableStateFlow(false)
    val isReviewOnly: StateFlow<Boolean> = _isReviewOnly.asStateFlow()

    private val _showSortDropdown = MutableStateFlow(false)
    val showSortDropdown: StateFlow<Boolean> = _showSortDropdown.asStateFlow()

    fun updateSort(sort: ReviewSort) {
        _sort.value = sort
        getMyReviews()
    }

    fun toggleReviewOnly() {
        _isReviewOnly.value = !_isReviewOnly.value
    }

    fun changeDropdownState() {
        _showSortDropdown.value = !_showSortDropdown.value
    }

    private val _reviewResponse = MutableStateFlow<MyReviewsResponse?>(null)
    val reviewResponse: StateFlow<MyReviewsResponse?> = _reviewResponse.asStateFlow()

    private val _reviews = MutableStateFlow<List<MyReviewItem>>(emptyList())
    val reviews: StateFlow<List<MyReviewItem>> = _reviews.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _hasMoreData = MutableStateFlow(false)

    init {
        getMyReviews()
    }

    fun getMyReviews(lastId: Int? = null) {
        if (lastId != null && (_isLoading.value || !_hasMoreData.value)) return

        if (lastId == null) {
            _reviewResponse.value = null
            _reviews.value = emptyList()
            _hasMoreData.value = true
        }

        _isLoading.value = true

        viewModelScope.launch {
            myPageRepository.getMyReviews(
                request = MyReviewsRequest(
                    lastId = _reviewResponse.value?.cursor?.lastId,
                    lastLikeCount = if (_sort.value == ReviewSort.LIKES) _reviewResponse.value?.cursor?.lastValue else null,
                    lastRating = if (_sort.value == ReviewSort.RATING_ASC || _sort.value == ReviewSort.RATING_DESC) _reviewResponse.value?.cursor?.lastValue else null,
                    sort = _sort.value.param,
                    reviewOnly = _isReviewOnly.value,
                    size = 10
                )
            ).fold(
                onSuccess = {
                    _reviewResponse.value = it
                    _reviews.value += it.reviews

                    _isLoading.value = false
                    _hasMoreData.value = it.reviews.size == 10
                },
                onFailure = {
                    _isLoading.value = false
                    // TODO
                }
            )
        }
    }

    fun deleteReview(reviewId: Int) {
        // TODO Loading 추가

        viewModelScope.launch {
            reviewRepository.deleteReview(reviewId).getOrThrow()
            getMyReviews()
        }
    }

    private val _isLikedLoading = MutableStateFlow(false)
    val isLikedLoading = _isLikedLoading.asStateFlow()
    fun updateLikeState(liked: Boolean, reviewId: Int, onResult: (Boolean) -> Unit) {
        if (_isLikedLoading.value) return

        _isLikedLoading.value = true

        viewModelScope.launch {
            reviewRepository.updateReviewLike(
                action = if (liked) ApiAction.CREATE else ApiAction.DELETE,
                reviewId = reviewId
            ).getOrThrow()

            onResult(true)
            _isLikedLoading.value = false
        }
    }
}