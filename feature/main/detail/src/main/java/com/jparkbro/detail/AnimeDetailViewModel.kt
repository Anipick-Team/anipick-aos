package com.jparkbro.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.detail.DetailRepository
import com.jparkbro.data.review.ReviewRepository
import com.jparkbro.domain.DetailDataUseCase
import com.jparkbro.model.common.ApiAction
import com.jparkbro.model.common.DefaultAnime

import com.jparkbro.model.common.WatchStatus
import com.jparkbro.model.detail.DetailActor
import com.jparkbro.model.detail.DetailData
import com.jparkbro.model.detail.DetailInfo
import com.jparkbro.model.detail.DetailMyReview
import com.jparkbro.model.detail.DetailReviewItem
import com.jparkbro.model.detail.DetailSeries
import com.jparkbro.model.detail.ReviewDetailRequest
import com.jparkbro.model.detail.ReviewDetailResponse
import com.jparkbro.model.detail.ReviewSort
import com.jparkbro.model.review.ReportReviewRequest
import com.jparkbro.model.review.ReviewRating
import com.jparkbro.ui.DialogData
import com.jparkbro.ui.SnackBarData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DetailAnimeViewModel.Factory::class)
class DetailAnimeViewModel @AssistedInject constructor(
    private val detailDataUseCase: DetailDataUseCase,
    private val detailRepository: DetailRepository,
    private val reviewRepository: ReviewRepository,
    @Assisted val animeId: Int,
    @ApplicationContext val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val _tabIndex = MutableStateFlow(DetailTab.ANIME_INFO)
    val tabIndex: StateFlow<DetailTab> = _tabIndex.asStateFlow()

    private val _sort = MutableStateFlow(ReviewSort.LATEST)
    val sort: StateFlow<ReviewSort> = _sort.asStateFlow()

    private val _showSortDropdown = MutableStateFlow(false)
    val showSortDropdown: StateFlow<Boolean> = _showSortDropdown.asStateFlow()

    private val _dialogData = MutableStateFlow<DialogData?>(null)
    val dialogData = _dialogData.asStateFlow()

    fun updateDialogData(data: DialogData? = null) {
        _dialogData.value = data
    }

    private val _includeSpoiler = MutableStateFlow(false)
    val includeSpoiler: StateFlow<Boolean> = _includeSpoiler.asStateFlow()

    fun toggleIncludeSpoiler() {
        _includeSpoiler.value = !_includeSpoiler.value
    }

    fun changeDropdownState() {
        _showSortDropdown.value = !_showSortDropdown.value
    }

    fun updateSort(sort: ReviewSort) {
        _sort.value = sort
        getAnimeReviews()
    }

    private val _snackBarData = MutableStateFlow<SnackBarData?>(null)
    val snackBarData: StateFlow<SnackBarData?> = _snackBarData.asStateFlow()

    fun updateSnackBarData(snackBarData: SnackBarData? = null) {
        _snackBarData.value = snackBarData
    }

    private val _detailInfo = MutableStateFlow<DetailInfo?>(null)
    val detailInfo: StateFlow<DetailInfo?> = _detailInfo.asStateFlow()

    private val _actors = MutableStateFlow<List<DetailActor>>(emptyList())
    val actors: StateFlow<List<DetailActor>> = _actors.asStateFlow()

    private val _series = MutableStateFlow<List<DetailSeries>>(emptyList())
    val series: StateFlow<List<DetailSeries>> = _series.asStateFlow()

    private val _recommendations = MutableStateFlow<List<DefaultAnime>>(emptyList())
    val recommendations: StateFlow<List<DefaultAnime>> = _recommendations.asStateFlow()

    private val _myReview = MutableStateFlow<DetailMyReview?>(null)
    val myReview: StateFlow<DetailMyReview?> = _myReview.asStateFlow()

    private val _reviewResponse = MutableStateFlow<ReviewDetailResponse?>(null)
    val reviewResponse: StateFlow<ReviewDetailResponse?> = _reviewResponse.asStateFlow()

    private val _reviewItems = MutableStateFlow<List<DetailReviewItem>>(emptyList())
    val reviewItems: StateFlow<List<DetailReviewItem>> = _reviewItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasMoreData = mutableStateOf(true)

    init {
        saveRecentAnime()
        getInitData()
    }

    private fun saveRecentAnime() {
        viewModelScope.launch {
            detailRepository.saveRecentAnime(animeId)
        }
    }

    fun getInitData() {
        _uiState.value = DetailUiState.Loading
        
        viewModelScope.launch {
            try {
                detailDataUseCase(animeId).collect { result ->
                    result.fold(
                        onSuccess = { detailData ->
                            _uiState.value = DetailUiState.Success
                            _detailInfo.value = detailData.info
                            _actors.value = detailData.actor
                            _series.value = detailData.series
                            _recommendations.value = detailData.recommendation
                            _myReview.value = detailData.myReview
                        },
                        onFailure = { exception ->
                            Log.e("AnimeDetailViewModel", "Failed to get detail data", exception)
                            _uiState.value = DetailUiState.Error(exception.message ?: "데이터 조회 중 에러 발생")
                        }
                    )
                }
                getAnimeReviews()
            } catch (e: Exception) {
                Log.e("AnimeDetailViewModel", "Unexpected error in getInitData", e)
                _uiState.value = DetailUiState.Error(e.message ?: "예상치 못한 오류가 발생했습니다")
            }
        }
    }

    fun getAnimeReviews(lastId: Int? = null) {
        if (lastId != null && (_isLoading.value || !_hasMoreData.value)) return

        if (lastId == null) {
            _reviewItems.value = emptyList()
        }

        _isLoading.value = true

        viewModelScope.launch {
            detailRepository.getDetailReviews(
                request = ReviewDetailRequest(
                    animeId = animeId,
                    sort = _sort.value.param,
                    isSpoiler = true,
                    lastValue = _reviewResponse.value?.cursor?.lastValue,
                    lastId = lastId,
                    size = 10
                )
            ).fold(
                onSuccess = {
                    _reviewResponse.value = it
                    _reviewItems.value += it.reviews
                    _hasMoreData.value = it.reviews.size == 10
                    _isLoading.value = false
                },
                onFailure = {
                    // TODO
                    _isLoading.value = false
                    _uiState.value = DetailUiState.Error("${it.message}")
                }
            )
        }
    }

    // 애니 평가
    private val _isAnimeRatingLoading = MutableStateFlow(false)
    val isAnimeRatingLoading: StateFlow<Boolean> = _isAnimeRatingLoading.asStateFlow()
    fun rateAnime(rating: Float, onResult: (Boolean) -> Unit) {
        if (_isAnimeRatingLoading.value) return

        _isAnimeRatingLoading.value = true

        viewModelScope.launch {
            val currentRating = _myReview.value?.rating

            val (action, animeId, reviewId) = when {
                currentRating == null -> Triple(ApiAction.CREATE, _detailInfo.value?.animeId, -1)
                rating == 0f -> Triple(ApiAction.DELETE, -1, _myReview.value?.reviewId)
                else -> Triple(ApiAction.UPDATE, -1, _myReview.value?.reviewId)
            }

            detailRepository.setAnimeRating(
                action = action,
                animeId = animeId ?: -1,
                reviewId = reviewId ?: -1,
                request = ReviewRating(rating)
            ).also {
                _isAnimeRatingLoading.value = false
            }.fold(
                onSuccess = {
                    getInitData()
                    onResult(true)
                },
                onFailure = {
                    onResult(false)
                }
            )
        }
    }

    private val _isLikeLoading = MutableStateFlow(false)
    val isLikeLoading: StateFlow<Boolean> = _isLikeLoading.asStateFlow()
    fun likeAnime(isLiked: Boolean) {
        if (_isLikeLoading.value) return

        viewModelScope.launch {
            _isLikeLoading.value = true
            _detailInfo.value = _detailInfo.value?.isLiked?.let { _detailInfo.value?.copy(isLiked = !it) }
            detailRepository.setLikeAnime(
                action = if (isLiked) {
                    ApiAction.DELETE
                } else {
                    ApiAction.CREATE
                },
                animeId = animeId
            ).fold(
                onSuccess = {
                    _isLikeLoading.value = false
                },
                onFailure = {
                    _detailInfo.value = _detailInfo.value?.isLiked?.let { _detailInfo.value?.copy(isLiked = !it) }
                    _isLikeLoading.value = false
                }
            )
        }
    }

    private val _isChangeStatusLoading = MutableStateFlow(false)
    val isChangeStatusLoading: StateFlow<Boolean> = _isChangeStatusLoading.asStateFlow()
    fun changeAnimeStatus(watchStatus: WatchStatus) {
        if (_isLikeLoading.value) return

        viewModelScope.launch {
            _isChangeStatusLoading.value = true
            val currentStatus = _detailInfo.value?.watchStatus

            _detailInfo.value = _detailInfo.value?.copy(watchStatus = watchStatus)
            detailRepository.setWatchStatus(
                action = when (currentStatus) {
                    null -> {
                        ApiAction.CREATE
                    }
                    watchStatus -> {
                        ApiAction.DELETE
                    }
                    else -> {
                        ApiAction.UPDATE
                    }
                },
                animeId = animeId,
                status = watchStatus
            ).fold(
                onSuccess = {
                    _isChangeStatusLoading.value = false
                    if (currentStatus == watchStatus) _detailInfo.value = _detailInfo.value?.copy(watchStatus = null)
                },
                onFailure = {
                    _detailInfo.value = _detailInfo.value?.copy(watchStatus = currentStatus)
                    _isChangeStatusLoading.value = false
                }
            )
        }
    }

    private val _isReviewLikedLoading = MutableStateFlow(false)
    val isReviewLikedLoading = _isReviewLikedLoading.asStateFlow()
    fun updateLikeState(liked: Boolean, reviewId: Int, onResult: (Boolean) -> Unit) {
        if (_isReviewLikedLoading.value) return

        _isReviewLikedLoading.value = true

        viewModelScope.launch {
            reviewRepository.updateReviewLike(
                action = if (liked) ApiAction.CREATE else ApiAction.DELETE,
                reviewId = reviewId
            ).fold(
                onSuccess = {
                    onResult(true)
                    _isReviewLikedLoading.value = false
                },
                onFailure = { exception ->
                    Log.e("AnimeDetailViewModel", "Failed to update review like", exception)
                    onResult(false)
                    _isReviewLikedLoading.value = false
                }
            )
        }
    }

    fun deleteReview(reviewId: Int) {
        viewModelScope.launch {
            reviewRepository.deleteReview(reviewId).fold(
                onSuccess = {
                    getInitData()
                },
                onFailure = { exception ->
                    Log.e("AnimeDetailViewModel", "Failed to delete review", exception)
                    _uiState.value = DetailUiState.Error(exception.message ?: "리뷰 삭제에 실패했습니다")
                }
            )
        }
    }

    fun reportReview(reviewId: Int, reason: String) {
        viewModelScope.launch {
            reviewRepository.reportReview(reviewId, ReportReviewRequest(message = reason)).fold(
                onSuccess = {
                    getInitData()
                    updateSnackBarData(SnackBarData("리뷰가 신고되었습니다"))
                },
                onFailure = { exception ->
                    Log.e("AnimeDetailViewModel", "Failed to report review", exception)
                    updateSnackBarData(SnackBarData("리뷰 신고에 실패했습니다"))
                }
            )
        }
    }

    fun blockUser(userId: Int) {
        viewModelScope.launch {
            reviewRepository.blockUser(userId).fold(
                onSuccess = {
                    getInitData()
                    updateSnackBarData(SnackBarData("사용자가 차단되었습니다"))
                },
                onFailure = { exception ->
                    Log.e("AnimeDetailViewModel", "Failed to block user", exception)
                    updateSnackBarData(SnackBarData("사용자 차단에 실패했습니다"))
                }
            )
        }
    }

    fun updateTabIndex(detailTab: DetailTab) {
        if (_tabIndex.value != detailTab) {
            _tabIndex.value = detailTab
        }
    }

    fun copyAnimeLink() {
        val deepLink = "anipick://anime/$animeId"
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("애니메이션 링크", deepLink)
        clipboard.setPrimaryClip(clip)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            animeId: Int
        ): DetailAnimeViewModel
    }
}

sealed interface DetailUiState {
    data object Loading: DetailUiState
    data object Success: DetailUiState
    data class Error(val msg: String): DetailUiState
}

enum class DetailTab {
    ANIME_INFO,
    REVIEWS
}