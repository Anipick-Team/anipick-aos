package com.jparkbro.anipick

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jparkbro.data.common.CommonRepository
import com.jparkbro.datastore.NoticeDataStore
import com.jparkbro.domain.AutoLoginUseCase
import com.jparkbro.network.interceptor.LogoutEventManager
import com.jparkbro.model.common.MetaData
import com.jparkbro.ui.model.DialogData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val commonRepository: CommonRepository,
    private val autoLoginUseCase: AutoLoginUseCase,
    private val logoutEventManager: LogoutEventManager,
    private val noticeDataStore: NoticeDataStore,
) : ViewModel() {

    private val _metaData = MutableStateFlow(MetaData())
    val metaData = _metaData.asStateFlow()

    private val _uiState = MutableStateFlow<MainActivityUiState>(MainActivityUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _dialogData = MutableStateFlow<DialogData?>(null)
    val dialogData = _dialogData.asStateFlow()

    private val _pendingDeepLink = MutableStateFlow<Uri?>(null)
    val pendingDeepLink = _pendingDeepLink.asStateFlow()

    private val _updateDownloaded = MutableStateFlow(false)
    val updateDownloaded = _updateDownloaded.asStateFlow()

    private val _showNoticeDialog = MutableStateFlow(false)
    val showNoticeDialog = _showNoticeDialog.asStateFlow()

    init {
        try {
            getMetaData()
            autoLogin()
            observeLogoutEvent()
            checkNotice()
        } catch (e: Exception) {
            Log.e("MainActivityViewModel", "Initialization error", e)
            _uiState.value = MainActivityUiState.Error(
                "앱 초기화 중 오류가 발생했습니다.\n${e.message}"
            )
        }
    }

    private fun observeLogoutEvent() {
        viewModelScope.launch {
            logoutEventManager.logoutEvent.collect {
                Log.d("MainActivityViewModel", "로그아웃 이벤트 수신 - Login 화면으로 이동")
                _uiState.value = MainActivityUiState.Success(isAutoLogin = false)
            }
        }
    }

    fun setPendingDeepLink(uri: Uri?) {
        Log.d("MainActivityViewModel", "Setting pending deep link: $uri")
        _pendingDeepLink.value = uri
    }

    fun clearPendingDeepLink() {
        Log.d("MainActivityViewModel", "Clearing pending deep link")
        _pendingDeepLink.value = null
    }

    fun setUpdateDownloaded(downloaded: Boolean) {
        Log.d("MainActivityViewModel", "Update downloaded state: $downloaded")
        _updateDownloaded.value = downloaded
    }

    private fun checkNotice() {
        viewModelScope.launch {
            _showNoticeDialog.value = !noticeDataStore.hasSeenNotice()
        }
    }

    fun dismissNoticeDialog() {
        _showNoticeDialog.value = false
        viewModelScope.launch {
            noticeDataStore.setNoticeSeen()
        }
    }

    fun retryAppInit() {
        _uiState.value = MainActivityUiState.Loading
        getMetaData()
        autoLogin()
    }

    private fun getMetaData() {
        viewModelScope.launch {
            try {
                commonRepository.getMetaData().fold(
                    onSuccess = { metaData ->
                        Log.d("MainActivityViewModel", "Successfully loaded meta data")
                        _metaData.value = metaData
                    },
                    onFailure = { exception ->
                        Log.e("MainActivityViewModel", "Failed to load meta data", exception)
                    }
                )
            } catch (e: Exception) {
                Log.e("MainActivityViewModel", "Unexpected error in getMetaData", e)
            }
        }
    }

    fun updateDialog() {

    }

    private fun autoLogin() {
        viewModelScope.launch {
            try {
                autoLoginUseCase().collect { result ->
                    result.fold(
                        onSuccess = { isAutoLogin ->
                            Log.d("MainActivityViewModel", "Auto login result: $isAutoLogin")
                            _uiState.value = MainActivityUiState.Success(isAutoLogin)
                        },
                        onFailure = { exception ->
                            Log.e("MainActivityViewModel", "Auto login failed", exception)
                            // 자동 로그인 실패 시 로그인 화면으로 이동 (false)
                            _uiState.value = MainActivityUiState.Success(false)
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("MainActivityViewModel", "Unexpected error in autoLogin", e)
                _uiState.value = MainActivityUiState.Success(false)
            }
        }
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val isAutoLogin: Boolean) : MainActivityUiState
    data class Error(val message: String) : MainActivityUiState

    fun shouldKeepSplashScreen() = this is Loading
}