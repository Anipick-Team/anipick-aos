package com.jparkbro.mypage

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.mypage.MyPageRepository
import com.jparkbro.domain.MyPageInfoUseCase
import com.jparkbro.model.mypage.MyPageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
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
        _uiState.value = MyPageUiState.Loading
        
        viewModelScope.launch {
            try {
                myPageInfoUseCase().collect { result ->
                    result.fold(
                        onSuccess = { myPageResponse ->
                            Log.d("MyPageViewModel", "Successfully loaded my page info")
                            _uiState.value = MyPageUiState.Success(myPageResponse)
                        },
                        onFailure = { exception ->
                            Log.e("MyPageViewModel", "Failed to load my page info", exception)
                            _uiState.value = MyPageUiState.Error(exception.message ?: "사용자 정보를 불러오는데 실패했습니다")
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("MyPageViewModel", "Unexpected error in getInfo", e)
                _uiState.value = MyPageUiState.Error(e.message ?: "예상치 못한 오류가 발생했습니다")
            }
        }
    }

    private val _isUploadLoading = MutableStateFlow(false)
    val isUploadLoading = _isUploadLoading.asStateFlow()

    fun editProfileImg(contentUri: Uri) {
        if (_isUploadLoading.value) return

        _isUploadLoading.value = true

        viewModelScope.launch {
            try {
                myPageRepository.editProfileImg(contentUri).fold(
                    onSuccess = {
                        Log.d("MyPageViewModel", "Profile image uploaded successfully")
                        getInfo() // 프로필 정보 새로고침
                    },
                    onFailure = { exception ->
                        Log.e("MyPageViewModel", "Failed to upload profile image", exception)
                        _uiState.value = MyPageUiState.Error(exception.message ?: "프로필 이미지 업로드에 실패했습니다")
                    }
                )
            } catch (e: Exception) {
                Log.e("MyPageViewModel", "Unexpected error in editProfileImg", e)
                _uiState.value = MyPageUiState.Error(e.message ?: "예상치 못한 오류가 발생했습니다")
            } finally {
                _isUploadLoading.value = false
            }
        }
    }
}

sealed interface MyPageUiState {
    data object Loading: MyPageUiState
    data class Success(val data: MyPageResponse): MyPageUiState
    data class Error(val msg: String): MyPageUiState
}