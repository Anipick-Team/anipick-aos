package com.jparkbro.mypage

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.mypage.MyPageRepository
import com.jparkbro.domain.MyPageInfoUseCase
import com.jparkbro.model.common.Result
import com.jparkbro.model.mypage.MyPageResponse
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageInfoUseCase: MyPageInfoUseCase,
    private val myPageRepository: MyPageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Loading)
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    // TODO state, data 분리

    init {
        getInfo()
    }

    fun getInfo() {
        viewModelScope.launch {

            myPageInfoUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.value = MyPageUiState.Loading
                    is Result.Error -> _uiState.value = MyPageUiState.Error("${result.exception.message}")
                    is Result.Success<MyPageResponse> -> {
                        _uiState.value = MyPageUiState.Success(result.data)
                    }
                }
            }
        }
    }

    private val _isUploadLoading = MutableStateFlow(false)
    val isUploadLoading = _isUploadLoading.asStateFlow()

    fun editProfileImg(contentUri: Uri) {
        if (_isUploadLoading.value) return

        _isUploadLoading.value = true

        viewModelScope.launch {
            myPageRepository.editProfileImg(contentUri).fold(
                onSuccess = {
                    getInfo()
                },
                onFailure = {
                    // TODO
                }
            )
            _isUploadLoading.value = false
        }
    }
}

sealed interface MyPageUiState {
    data object Loading: MyPageUiState
    data class Success(val data: MyPageResponse): MyPageUiState
    data class Error(val msg: String): MyPageUiState
}