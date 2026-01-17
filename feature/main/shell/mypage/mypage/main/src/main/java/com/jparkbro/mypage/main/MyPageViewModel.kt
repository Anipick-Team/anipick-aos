package com.jparkbro.mypage.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.user.UserRepository
import com.jparkbro.model.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MyPageState())
    val state = _state.asStateFlow()

    private val _eventChannel = Channel<MyPageEvent>()
    val events = _eventChannel.receiveAsFlow()

    init {
        collectUserInfo()
        initDataLoad()
    }

    fun onAction(action: MyPageAction) {
        when (action) {
            MyPageAction.OnRetryClicked -> {
                viewModelScope.launch(Dispatchers.IO) { userRepository.refreshUserInfo() }
            }
            is MyPageAction.OnChangeProfileImage -> updateProfileImage(action.uri)
        }
    }

    private fun collectUserInfo() {
        viewModelScope.launch(Dispatchers.Main) {
            userRepository.getUserInfo().collect { userInfo ->
                _state.update {
                    it.copy(
                        nickname = userInfo?.nickname,
                        profileImageByteArray = userInfo?.profileImageBytes,
                        watchCounts = userInfo?.watchCounts,
                        likeAnimes = userInfo?.likedAnimes ?: emptyList(),
                        likeActors = userInfo?.likedPersons ?: emptyList()
                    )
                }
            }
        }
    }

    private fun initDataLoad() {
        _state.update { it.copy(uiState = UiState.Loading) }

        viewModelScope.launch(Dispatchers.IO) {
            userRepository.loadUserInfo()
                .fold(
                    onSuccess = { _state.update { it.copy(uiState = UiState.Success) } },
                    onFailure = { _state.update { it.copy(uiState = UiState.Error) } }
                )
        }
    }

    private fun updateProfileImage(contentUri: Uri) {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateProfileImage(
                contentUri = contentUri
            ).onFailure {
                // TODO toast
            }

            _state.update { it.copy(isLoading = false) }
        }
    }
}
