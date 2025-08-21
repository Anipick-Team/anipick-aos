package com.jparkbro.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.UserPreferenceRepository
import com.jparkbro.data.detail.DetailRepository
import com.jparkbro.data.home.HomeRepository
import com.jparkbro.data.review.ReviewRepository
import com.jparkbro.model.common.ApiAction
import com.jparkbro.model.home.ContentType
import com.jparkbro.model.home.HomeDetailRequest
import com.jparkbro.model.home.HomeDetailResponse
import com.jparkbro.model.home.Sort
import com.jparkbro.model.review.ReportReviewRequest
import com.jparkbro.ui.DialogData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = HomeDetailViewModel.Factory::class)
class HomeDetailViewModel @AssistedInject constructor(
    private val homeRepository: HomeRepository,
    private val detailRepository: DetailRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val reviewRepository: ReviewRepository,
    @Assisted val type: ContentType
) : ViewModel() {

    private val _animeId = mutableStateOf<Int?>(null)

    private val _nickname = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname.asStateFlow()

    private val _sort = MutableStateFlow(Sort.LATEST)
    val sort: StateFlow<Sort> = _sort.asStateFlow()

    private val _showSortDropdown = MutableStateFlow(false)
    val showSortDropdown: StateFlow<Boolean> = _showSortDropdown.asStateFlow()

    private val _includeAdult = MutableStateFlow(false)
    val includeAdult: StateFlow<Boolean> = _includeAdult.asStateFlow()

    private val _dialogData = MutableStateFlow<DialogData?>(null)
    val dialogData = _dialogData.asStateFlow()

    fun updateDialogData(data: DialogData? = null) {
        _dialogData.value = data
    }

    private val _uiState = MutableStateFlow<HomeDetailUiState>(HomeDetailUiState.Loading)
    val uiState: StateFlow<HomeDetailUiState> = _uiState.asStateFlow()

    private val _responseData = MutableStateFlow<HomeDetailResponse?>(null)
    val responseData: StateFlow<HomeDetailResponse?> = _responseData.asStateFlow()

    private val _items = MutableStateFlow<List<Any>>(emptyList())
    val items: StateFlow<List<Any>> = _items.asStateFlow()

    fun toggleIncludeAdult() {
        _includeAdult.value = !_includeAdult.value
    }

    fun changeDropdownState() {
        _showSortDropdown.value = !_showSortDropdown.value
    }

    fun updateSort(sort: Sort) {
        _sort.value = sort
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasMoreData = mutableStateOf(true)

    init {
        getReferenceAnimeId()
    }

    private fun getReferenceAnimeId() {
        viewModelScope.launch {
            _animeId.value = detailRepository.loadRecentAnime().getOrNull()
            userPreferenceRepository.getUserNickName().onSuccess {
                _nickname.value = it
            }
            loadData()
        }
    }

    fun loadData(lastId: Int? = null) {
        val isInitialLoad = _uiState.value == HomeDetailUiState.Loading

        if (lastId != null && (_isLoading.value || !_hasMoreData.value)) return

        viewModelScope.launch {
            if (!isInitialLoad) { _isLoading.value = true }
            homeRepository.getDetailData(
                type = type,
                request = HomeDetailRequest(
                    animeId = _animeId.value,
                    lastId = _responseData.value?.cursor?.lastId,
                    lastValue = _responseData.value?.cursor?.lastValue,
                    sort = _sort.value.param,
                    includeAdult = _includeAdult.value,
                    size = 18
                ),
            ).fold(
                onSuccess = {
                    if (isInitialLoad) {
                        _uiState.value = HomeDetailUiState.Success
                    } else {
                        _isLoading.value = false
                    }
                    _responseData.value = it

                    val items = if (type == ContentType.RECENT_REVIEW) it.reviews else it.animes

                    _items.value += items
                    _hasMoreData.value = items.size == 18
                },
                onFailure = {
                    if (isInitialLoad) {
                        _uiState.value = HomeDetailUiState.Error("${it.message}")
                    } else {
                        _isLoading.value = false
                    }
                }
            )
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

    fun deleteReview(reviewId: Int) {
        // TODO Loading 추가

        viewModelScope.launch {
            reviewRepository.deleteReview(reviewId).getOrThrow()
            loadData()
        }
    }

    fun reportReview(reviewId: Int, reason: String) {
        viewModelScope.launch {
            reviewRepository.reportReview(reviewId, ReportReviewRequest(reason)).getOrThrow()
            loadData()
        }
    }

    fun blockUser(userId: Int) {
        viewModelScope.launch {
            reviewRepository.blockUser(userId).getOrThrow()
            loadData()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            type: ContentType
        ): HomeDetailViewModel
    }
}

sealed interface HomeDetailUiState {
    data object Loading: HomeDetailUiState
    data object Success: HomeDetailUiState
    data class Error(val msg: String): HomeDetailUiState
}