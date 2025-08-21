package com.jparkbro.mypage

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.mypage.MyPageRepository
import com.jparkbro.model.mypage.ContentType
import com.jparkbro.model.mypage.UserContentData
import com.jparkbro.model.mypage.UserContentResponse
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = UserContentViewModel.Factory::class)
class UserContentViewModel @AssistedInject constructor(
    private val myPageRepository: MyPageRepository,
    @Assisted val type: ContentType
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserContentUiState>(UserContentUiState.Loading)
    val uiState: StateFlow<UserContentUiState> = _uiState.asStateFlow()

    private val _responseData = MutableStateFlow<UserContentResponse?>(null)
    val responseData: StateFlow<UserContentResponse?> = _responseData.asStateFlow()

    private val _dataList = MutableStateFlow<List<UserContentData>>(emptyList())
    val dataList: StateFlow<List<UserContentData>> = _dataList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasMoreData = mutableStateOf(true)

    init {
        loadData()
    }

    fun loadData(lastId: Int? = null) {
        val isInitialLoad = lastId == null && _dataList.value.isEmpty()

        if (_isLoading.value || !_hasMoreData.value) return

        viewModelScope.launch {
            if (isInitialLoad) _uiState.value = UserContentUiState.Loading

            _isLoading.value = true

            myPageRepository.getUserContents(
                type = type,
                lastId = lastId
            ).fold(
                onSuccess = {
                    if (isInitialLoad) _uiState.value = UserContentUiState.Success

                    _isLoading.value = false
                    _responseData.value = it    // cursor, count 값 가지고 있는 객체
                    _dataList.value = when (type) {
                        ContentType.LIKED_PERSON -> _dataList.value + it.persons
                        else -> _dataList.value + it.animes
                    }
                },
                onFailure = {
                    _isLoading.value = false
                    // TODO
                }
            )
        }
    }

    fun refreshData() {
        _hasMoreData.value = true
        _dataList.value = emptyList()

        loadData()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            type: ContentType
        ): UserContentViewModel
    }
}

sealed interface UserContentUiState {
    data object Loading: UserContentUiState
    data object Success: UserContentUiState
    data class Error(val msg: String): UserContentUiState
}