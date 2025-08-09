package com.jparkbro.mypage

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.mypage.MyPageRepository
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
    private val myPageRepository: MyPageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Loading)
    val uiState: StateFlow<MyPageUiState> = _uiState.asStateFlow()

    // TODO state, data 분리

    private val _profileImage = MutableStateFlow<String?>(null)
    val profileImage = _profileImage.asStateFlow()

    init {
        getInfo()
    }

    private fun getInfo() {
        viewModelScope.launch {
            _uiState.value = MyPageUiState.Loading

            myPageRepository.getMyPageInfo().fold(
                onSuccess = {
                    _uiState.value = MyPageUiState.Success(it)
                    _profileImage.value = it.profileImageUrl
                },
                onFailure = {
                    // TODO
                }
            )
        }
    }

    fun editProfileImg(contentUri: Uri) {
        viewModelScope.launch {
            myPageRepository.editProfileImg(contentUri).fold(
                onSuccess = {
                    _profileImage.value = it.profileImageUrl
                },
                onFailure = {
                    // TODO
                }
            )
        }
    }
}

sealed interface MyPageUiState {
    data object Loading: MyPageUiState
    data class Success(val data: MyPageResponse): MyPageUiState
    data class Error(val msg: String): MyPageUiState
}